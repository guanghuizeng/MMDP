package io.guanghuizeng.mmdp.algs;

import io.guanghuizeng.fs.utils.FileInputBuffer;
import io.guanghuizeng.fs.utils.FileOutputBuffer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by guanghuizeng on 16/3/13.
 */
public class ExternalSort {

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

    public static File sort(File input, File output) throws IOException {
        File tmpDirectory = null;
        Comparator<Long> comparator = Comparator.naturalOrder();
        mergeSortedFiles(output, sortInBatch(input, tmpDirectory, comparator));
        return output;
    }


    public static File sort(File input, File output, Comparator comparator) throws IOException {
        File tmpDirectory = null;
        mergeSortedFiles(output, sortInBatch(input, tmpDirectory, comparator));
        return output;
    }

    /**
     * list -> temporary file
     */
    private static File sortAndSave(List<Long> tmpList, File tmpDirectory, Comparator<Long> comparator) {
        Collections.sort(tmpList, comparator);
        try {
            File newTmpFile = File.createTempFile("sortInBatch", "flatfile", tmpDirectory);
            newTmpFile.deleteOnExit();
            FileOutputBuffer outputBuffer = new FileOutputBuffer(newTmpFile);

            for (Long number : tmpList) {
                outputBuffer.writeLong(number);
            }
            outputBuffer.close();
            return newTmpFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * file -> list of sorted files
     * 将一个文件划分为多个区块, 分别排序, 然后保存到多个临时文件中
     */
    private static List<File> sortInBatch(File file, File tmpDirectory, Comparator comparator) {
        List<File> files = new ArrayList<>();

        // size of a individual file in byte
        long blockSize = bestSizeOfBlock(file.length(), 1024, availableMemory());

        try {
            FileInputBuffer inputBuffer = new FileInputBuffer(file);
            while (!inputBuffer.empty()) {
                List<Long> tmpList = new ArrayList<>();
                int counter = 0;
                while (!inputBuffer.empty() && counter < blockSize) {
                    tmpList.add(inputBuffer.pop());
                    counter = counter + Long.BYTES;
                }
                files.add(sortAndSave(tmpList, tmpDirectory, comparator));
                tmpList.clear();
            }
            inputBuffer.close();
            return files;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * buffers -> a file as result
     *
     * @param outputBuffer
     * @param inputBuffers
     * @return
     * @throws IOException
     */
    private static long mergeSortedFiles(FileOutputBuffer outputBuffer,
                                         List<FileInputBuffer> inputBuffers) throws IOException {

        /* a priority queue of numbers from buffers -> output file */
        PriorityQueue<FileInputBuffer> bufferPQ = new PriorityQueue<>();
        /* initialize queue */
        for (FileInputBuffer buffer : inputBuffers) {
            if (!buffer.empty()) {
                bufferPQ.add(buffer);
            }
        }

        long count = 0;
        try {
            while (bufferPQ.size() > 0) {
                FileInputBuffer input = bufferPQ.poll();
                outputBuffer.writeLong(input.pop());
                ++count;

                if (input.empty()) {
                    input.close();
                } else {
                    bufferPQ.add(input);
                }
            }
        } finally {
            outputBuffer.close();
        }
        return count;
    }

    private static long mergeSortedFiles(File outputFile, List<File> inputFiles) throws IOException {
        /* sorted files -> buffers */
        List<FileInputBuffer> inputBuffers = new ArrayList<>();
        FileOutputBuffer outputBuffer = new FileOutputBuffer(outputFile);

        for (File file : inputFiles) {
            FileInputBuffer buffer = new FileInputBuffer(file);
            inputBuffers.add(buffer);
        }

        /* buffers -> a file as result */
        return mergeSortedFiles(outputBuffer, inputBuffers);
    }


}
