package io.guanghuizeng.mmdp.engine;

import io.guanghuizeng.mmdp.utils.ObjectInputBuffer;
import io.guanghuizeng.mmdp.algs2.Histogram;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by guanghuizeng on 16/4/21.
 */
public class EngineBackend {

    public Histogram execute(MedianTaskSpec task) throws IOException {

        /**
         * 根据 task, 选择合适的 phase 处理函数
         */
        assert task.opcode() == Opcode.MEDIAN; // TODO throw an exception
        Path path = task.path();
        switch (task.phase()) {
            case FIRST:
                return phase1(path);
            case SECOND:
                return phase2(path, task.getFirst());
            case THIRD:
                return phase3(path, task.getFirst(), task.getSecond());
            case FOURTH:
                return phase4(path, task.getFirst(), task.getSecond(), task.getThird());
            default:
                return null; // TODO throw an exception
        }
    }

    public Histogram phase1(Path path) throws IOException {

        int countOfRange = (int) Math.pow(2, 16); // 分区个数
        ObjectInputBuffer buffer = new ObjectInputBuffer(path);
        Histogram histogram = new Histogram(countOfRange);

        while (!buffer.empty()) {
            long n = buffer.pop();
            histogram.add(partOfLong(n, 0));
        }
        return histogram;
    }

    public Histogram phase2(Path path, long firstIndex) throws IOException {

        int countOfRange = (int) Math.pow(2, 16); // 分区个数
        ObjectInputBuffer buffer = new ObjectInputBuffer(path);
        Histogram histogram = new Histogram(countOfRange);

        while (!buffer.empty()) {
            long n = buffer.pop();
            if (partOfLong(n, 0) == firstIndex) {
                histogram.add(partOfLong(n, 1));
            }
        }
        return histogram;
    }

    public Histogram phase3(Path path, long firstIndex, long secondIndex) throws IOException {

        int countOfRange = (int) Math.pow(2, 16); // 分区个数
        ObjectInputBuffer buffer = new ObjectInputBuffer(path);
        Histogram histogram = new Histogram(countOfRange);

        while (!buffer.empty()) {
            long n = buffer.pop();
            if (partOfLong(n, 0) == firstIndex
                    && partOfLong(n, 1) == secondIndex) {
                histogram.add(partOfLong(n, 2));
            }
        }
        return histogram;
    }

    public Histogram phase4(Path path, long firstIndex, long secondIndex, long thirdIndex) throws IOException {

        int countOfRange = (int) Math.pow(2, 16);// 分区个数
        ObjectInputBuffer buffer = new ObjectInputBuffer(path);
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
}

