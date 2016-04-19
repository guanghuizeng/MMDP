package io.guanghuizeng.mmdp.algs;

import com.google.common.primitives.Longs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by guanghuizeng on 16/4/18.
 */
public class ExternalSort3 {

    public static long last = 0;
    public static long present = 0;

    public static void sort(Path in, Path out, Comparator cmp) throws IOException {

        System.out.printf("Sorting: \n\tin - %s, \n\tout - %s\n", in.toString(), out.toString());

        /*****/
        last = present;
        present = System.currentTimeMillis();
        System.out.printf("%d, Start sort...\n", present - last);
        /*****/

        Path tmp = Paths.get(out.getParent().toString());
        mergeSortedFiles(sortInBatch(in, tmp), out);

        System.out.printf("Sorted: \n\tin - %s, \n\tout - %s\n", in.toString(), out.toString());
    }

    private static Path sortAndSave(List<Long> tmpList, Path tmpDir) throws IOException {

        /*****/
        last = present;
        present = System.currentTimeMillis();
        System.out.printf("%d, sortAndSave...\n", present - last);
        /*****/

        Collections.sort(tmpList, Comparator.naturalOrder()); // TODO 增加可选项

        // create tmp files
        Path newTmpFile = Files.createTempFile(tmpDir, "sortedInBatch", "flatFile");
        FileOutputBuffer outputBuffer = new FileOutputBuffer(newTmpFile);

        for (Long n : tmpList) {
            outputBuffer.writeLong(n);
        }
        outputBuffer.close();
        return newTmpFile;
    }

    private static List<Path> sortInBatch(Path in, Path tmpDir) throws IOException {
        /*****/
        last = present;
        present = System.currentTimeMillis();
        System.out.printf("%d, sortInBatch...\n", present - last);
        /*****/

        // in -> in channel
        File inFile = in.toFile();

        // size of a individual file in byte
        long blockSize = bestSizeOfBlock(inFile.length(), 1024, availableMemory());

        List<Path> tmpPathList = new ArrayList<>();
        FileInputBuffer inputBuffer = new FileInputBuffer(in);

        while (!inputBuffer.empty()) {
            List<Long> tmpList = new ArrayList<>();
            int counter = 0;
            while (!inputBuffer.empty() && counter < blockSize) {
                tmpList.add(inputBuffer.pop());
                counter = counter + Long.BYTES;
            }
            tmpPathList.add(sortAndSave(tmpList, tmpDir));
            tmpList.clear();
        }
        inputBuffer.close();
        return tmpPathList;
    }

    private static long mergeSortedFiles(List<Path> in, Path out) throws IOException {
        /*****/
        last = present;
        present = System.currentTimeMillis();
        System.out.printf("%d, mergeSortedFiles 1...\n", present - last);
        /*****/

        List<FileInputBuffer> inputBufferList = new ArrayList<>();
        for (Path p : in) {
            FileInputBuffer buffer = new FileInputBuffer(p);
            inputBufferList.add(buffer);
        }
        FileOutputBuffer outputBuffer = new FileOutputBuffer(out);
        return mergeSortedFiles(inputBufferList, outputBuffer);
    }

    private static long mergeSortedFiles(List<FileInputBuffer> inputBuffers, FileOutputBuffer outputBuffer)
            throws IOException {
        /*****/
        last = present;
        present = System.currentTimeMillis();
        System.out.printf("%d, mergeSortedFiles 2...\n", present - last);
        /*****/
        PriorityQueue<FileInputBuffer> bufferPQ = new PriorityQueue<>();
        for (FileInputBuffer buffer : inputBuffers) {
            if (!buffer.empty()) {
                bufferPQ.add(buffer);
            }
        }
        try {
            while (bufferPQ.size() > 0) {
                FileInputBuffer input = bufferPQ.poll();
                outputBuffer.writeLong(input.pop());
                if (input.empty()) {
                    input.close();
                } else {
                    bufferPQ.add(input);
                }
            }
        } finally {
            outputBuffer.close();
        }

        /*****/
        last = present;
        present = System.currentTimeMillis();
        System.out.printf("%d, mergeSortedFiles 2...\n", present - last);
        /*****/
        return 0;
    }


    /********
     * utils
     ********/

    /**
     * 计算可用的内存大小,以byte为单位
     *
     * @return 返回可用的内存大小, 以byte为单位
     */

    private static long availableMemory() {
        System.gc();
        return Runtime.getRuntime().freeMemory();
    }

    /**
     * 计算区块的size, 单位为byte
     */
    private static long bestSizeOfBlock(long sizeOfFile, long maxOfTmpFiles, long maxOfMemory) {

        long blockSize = sizeOfFile / maxOfTmpFiles + (sizeOfFile % maxOfTmpFiles == 0 ? 0 : 1);

        /* 尽量多读入一些数据, 在内存中排好序  */
        if (blockSize <= maxOfMemory / 2) {
            blockSize = maxOfMemory / 2;
        }
        return blockSize;
    }
}

class FileOutputBuffer {

    private BufferedOutputStream stream;

    public FileOutputBuffer(Path path) throws IOException {
        stream = new BufferedOutputStream(new FileOutputStream(path.toFile()));
    }

    public void writeLong(long n) throws IOException {
        byte[] bytes = Longs.toByteArray(n);
        stream.write(bytes);
        bytes = null;
    }

    public void close() throws IOException {
        stream.close();
    }
}

class FileInputBuffer implements Comparable<FileInputBuffer> {

    private BufferedInputStream stream;
    private boolean isEmpty = true;
    private long buffer;

    public FileInputBuffer(Path path) throws IOException {
        stream = new BufferedInputStream(new FileInputStream(path.toFile()));
        reload();
    }

    public boolean empty() {
        return isEmpty;
    }

    public void close() throws IOException {
        stream.close();
    }

    public Long peek() {
        return buffer;
    }

    public Long pop() throws IOException {
        Long value = buffer;
        reload();
        return value;
    }

    private void reload() throws IOException {
        if (stream.available() >= Long.BYTES) {
            byte[] in = new byte[Long.BYTES];
            stream.read(in);
            buffer = Longs.fromByteArray(in);
            in = null;
            isEmpty = false;
        } else {
            isEmpty = true;
        }
    }

    // TODO 增加控制选项
    public int compareTo(FileInputBuffer that) {
        return peek().compareTo(that.peek());
    }
}
