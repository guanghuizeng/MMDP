package io.guanghuizeng.mmdp.algs;

import io.guanghuizeng.mmdp.utils.FileInputBuffer;
import io.guanghuizeng.mmdp.utils.FileOutputBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by guanghuizeng on 16/3/20.
 */
public class FrequentNumbersFinder {

    public File find(File input, File output, int k) throws IOException {
        /* count in batch */
        List<File> tmpFiles = countInBatch(input);
        /* merge */
        output = merge(tmpFiles, output, k);
        return output;
    }

    public List<File> countInBatch(File data) throws IOException {

        List<File> files = new ArrayList<>();
        // size of a individual file in byte
        long blockSize = bestSizeOfBlock(data.length(), 1024, availableMemory());

        /* 多次调用count */
        FileInputBuffer buffer = new FileInputBuffer(data);
        while (!buffer.empty()) {

            List<Long> tmp = new ArrayList<>();
            int counter = 0;
            while (!buffer.empty() && counter < blockSize) {
                tmp.add(buffer.pop());
                counter += Long.BYTES;
            }
            files.add(count(tmp));
            tmp.clear();
        }
        buffer.close();
        return files;
    }

    /**
     * 统计数字出现频率, 将结果保存到输出文件
     *
     * @param data   输入数据
     * @param output 输出结果
     * @return 保存结果的文件
     * @throws IOException
     */
    public File count(File data, File output) throws IOException {
        Statistics statistics = new Statistics();
        Map<Long, Integer> frequency = statistics.frequency(data);
        save(frequency, output);
        return output;
    }

    public File count(List<Long> data, File output) throws IOException {
        Statistics statistics = new Statistics();
        Map<Long, Integer> frequency = statistics.frequency(data);
        save(frequency, output);
        return output;
    }

    public File count(List<Long> data) throws IOException {
        File newTmpFile = File.createTempFile("sortInBatch", "flatfile");
        newTmpFile.deleteOnExit();
        Statistics statistics = new Statistics();
        Map<Long, Integer> frequency = statistics.frequency(data);
        save(frequency, newTmpFile);
        return newTmpFile;
    }

    /**
     * 将统计信息保存到文件
     */
    private void save(Map<Long, Integer> data, File file) throws IOException {
        FileOutputBuffer buffer = new FileOutputBuffer(file);
        for (long key : data.keySet()) {
            buffer.writeLong(key);
            buffer.writeInt(data.get(key));
        }
        buffer.close();
    }

    /** ??
     *
     * @param input
     * @param output
     * @param k
     * @return
     * @throws IOException
     */
    public File merge(List<File> input, File output, int k) throws IOException {

        FileOutputBuffer outputBuffer = new FileOutputBuffer(output);
        PriorityQueue<InputBuffer> bufferPQ = new PriorityQueue<>();

        for (File file : input) {
            bufferPQ.add(new InputBuffer(file));
        }

        try {
            int count = 0;
            while (bufferPQ.size() > 0 && count < k) {
                InputBuffer buffer = bufferPQ.poll();
                Record record = buffer.pop();
                outputBuffer.writeLong(record.key);
                outputBuffer.writeInt(record.value);
                ++count;

                if (buffer.empty()) {
                    buffer.close();
                } else {
                    bufferPQ.add(buffer);
                }
            }
        } finally {
            outputBuffer.close();
        }
        return output;
    }


    /**
     * 计算可用的内存大小,以byte为单位
     *
     * @return 返回可用的内存大小, 以byte为单位
     */

    public static long availableMemory() {
        System.gc();
        return Runtime.getRuntime().freeMemory();
    }

    /**
     * 计算区块的size, 单位为byte
     */
    public static long bestSizeOfBlock(long sizeOfFile, long maxOfTmpFiles, long maxOfMemory) {

        long blockSize = sizeOfFile / maxOfTmpFiles + (sizeOfFile % maxOfTmpFiles == 0 ? 0 : 1);

        /* 尽量多读入一些数据, 在内存中排好序  */
        if (blockSize <= maxOfMemory / 2) {
            blockSize = maxOfMemory / 2;
        }
        return blockSize;
    }

}

class Record {
    Long key;
    Integer value;
    final Integer BYTES = Long.BYTES + Integer.BYTES;

    Record() {
    }

    Record(Long key, Integer value) {
        this.key = key;
        this.value = value;
    }
}

class InputBuffer implements Comparable<InputBuffer> {


    private ObjectInputStream buffer;
    private Record cache = new Record();

    InputBuffer(File file) throws IOException {
        this.buffer = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
        reload();
    }

    public void close() throws IOException {
        buffer.close();
    }

    public boolean empty() {
        return cache == null;
    }

    public Integer peek() {
        return cache.value;
    }

    public Record pop() throws IOException {
        Record answer = new Record(cache.key, cache.value);
        reload();
        return answer;
    }

    private void reload() throws IOException {
        if (buffer.available() > cache.BYTES) {
            cache.key = buffer.readLong();
            cache.value = buffer.readInt();
        } else {
            cache.key = null;
            cache.value = null;
        }
    }

    public int compareTo(InputBuffer that) {
        return peek().compareTo(that.peek());
    }
}