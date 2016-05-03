package io.guanghuizeng.mmdp;

import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.mmdp.algs.MinHeap;
import io.guanghuizeng.mmdp.algs2.ExternalSort;
import io.guanghuizeng.mmdp.algs2.FileInputBuffer;
import io.guanghuizeng.mmdp.algs2.FileOutputBuffer;
import io.guanghuizeng.mmdp.algs2.Histogram;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 实现服务器端的业务逻辑, 面向 sub task 设计.
 * TODO 线程安全
 */
public class EngineBackendExecutor {

    private FileSystem fileSystem = new FileSystem();

    public SortSubTaskSpec exec(SortSubTaskSpec spec) throws IOException {

        ExternalSort.sort(
                Paths.get(fileSystem.getHome(spec.getInput().getServiceID().code()),
                        spec.getInput().getActualPath().getLocalPath().toString()),
                Paths.get(fileSystem.getHome(spec.getOutput().getServiceID().code()),
                        spec.getOutput().getActualPath().getLocalPath().toString()),
                Comparator.naturalOrder());

        return spec;
    }

    public MedianSubTaskSpec exec(MedianSubTaskSpec spec) throws IOException {

        /**
         * 根据 task, 选择合适的 phase 处理函数
         */
        assert spec.getOpcode() == Opcode.MEDIAN; // TODO throw an exception. 并没有用...
        Path path = Paths.get(fileSystem.getHome(spec.getInput().getServiceID().code()),
                spec.getInput().getActualPath().getLocalPath().toString());

        Histogram result;
        switch (spec.getPhase()) {
            case FIRST:
                result = phase1(path);
                break;
            case SECOND:
                result = phase2(path, spec.getFirst());
                break;
            case THIRD:
                result = phase3(path, spec.getFirst(), spec.getSecond());
                break;
            case FOURTH:
                result = phase4(path, spec.getFirst(), spec.getSecond(), spec.getThird());
                break;
            default:
                return null; // TODO throw an exception
        }
        spec.setHistogram(result);
        return spec;
    }

    public Histogram phase1(Path path) throws IOException {
        try {
            int countOfRange = (int) Math.pow(2, 16); // 分区个数
            FileInputBuffer buffer = new FileInputBuffer(path);
            Histogram histogram = new Histogram(countOfRange);

            while (!buffer.empty()) {
                long n = buffer.pop();
                histogram.add(partOfLong(n, 0));
            }
            return histogram;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e.getCause());
        }
    }

    public Histogram phase2(Path path, long firstIndex) throws IOException {
        try {
            int countOfRange = (int) Math.pow(2, 16); // 分区个数
            FileInputBuffer buffer = new FileInputBuffer(path);
            Histogram histogram = new Histogram(countOfRange);

            while (!buffer.empty()) {
                long n = buffer.pop();
                if (partOfLong(n, 0) == firstIndex) {
                    histogram.add(partOfLong(n, 1));
                }
            }
            return histogram;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e.getCause());
        }
    }

    public Histogram phase3(Path path, long firstIndex, long secondIndex) throws IOException {

        try {
            int countOfRange = (int) Math.pow(2, 16); // 分区个数
            FileInputBuffer buffer = new FileInputBuffer(path);
            Histogram histogram = new Histogram(countOfRange);
            while (!buffer.empty()) {
                long n = buffer.pop();
                if (partOfLong(n, 0) == firstIndex
                        && partOfLong(n, 1) == secondIndex) {
                    histogram.add(partOfLong(n, 2));
                }
            }
            return histogram;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e.getCause());
        }

    }

    public Histogram phase4(Path path, long firstIndex, long secondIndex, long thirdIndex) throws IOException {

        int countOfRange = (int) Math.pow(2, 16);// 分区个数
        FileInputBuffer buffer = new FileInputBuffer(path);
        Histogram histogram = new Histogram(countOfRange);

        while (!buffer.empty()) {
            long n = buffer.pop();
            if (partOfLong(n, 0) == firstIndex
                    && partOfLong(n, 1) == secondIndex
                    && partOfLong(n, 2) == thirdIndex) {
                histogram.add(partOfLong(n, 3));
            }
        }
        return histogram;
    }

    private int partOfLong(long n, int index) {
        switch (index) {
            case 0:
                return (int) (n >> 48) & 0xFFFF;
            case 1:
                return (int) ((n >> 32) & 0xFFFF);
            case 2:
                return (int) (n >> 16 & 0xFFFF);
            case 3:
                return (int) n & 0xFFFF;
            default:
                return (int) n & 0xFFFF;  /* TODO: 再优化 */
        }
    }

    /**
     * 查找前 K 项最大值, 结果   以 naturalOrder 排序
     */
    public MaxSubTaskSpec exec(MaxSubTaskSpec spec) throws IOException {

        Path path = Paths.get(fileSystem.getHome(spec.getInput().getServiceID().code()),
                spec.getInput().getActualPath().getLocalPath().toString());
        FileInputBuffer buffer = new FileInputBuffer(path);
        int count = spec.getCount();

        // 应用 max heap 筛选文件中的数据
        MinHeap heap = new MinHeap(count);

        while (!buffer.empty()) {
            long value = buffer.pop();
            if (heap.getHeapSize() < count) {
                heap.insert(value);
            } else {
                if (value > heap.min()) {
                    heap.extract();
                    heap.insert(value);
                }
            }
        }
        buffer.close();

        // 逆向排序
        List<Long> result = new ArrayList<>();
        int size = heap.getHeapSize();
        for (int i = 0; i < size; i++) {
            result.add(heap.extract());
        }
        result.sort(Comparator.reverseOrder());

        // 写入到文件
        Path out = Paths.get(fileSystem.getHome(spec.getOutput().getServiceID().code()),
                spec.getOutput().getActualPath().getLocalPath().toString());
        FileOutputBuffer outputBuffer = new FileOutputBuffer(out);

        for (long n : result) {
            outputBuffer.writeLong(n);
        }
        outputBuffer.close();

        return spec;
    }
}