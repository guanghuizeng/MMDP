package io.guanghuizeng.mmdp.algs2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guanghuizeng on 16/3/16.
 * 基于整数的编码方式实现快速查找, 与Quickselect相比, 该方式的适用范围较小
 * <p>
 * TODO: 处理负数的问题
 */
public class MedianFinder64 {

    private class Pair {
        long count = 0;  /* 频度 */
        int index = 0;   /* 区间 */

        Pair(long count, int index) {
            this.count = count;
            this.index = index;
        }
    }

    /**
     * 用作统计数据的频度
     * 将数据分为多个区间, 统计每个区间中数据的频度
     */
    private class Histogram {

        List<Long> data = new ArrayList<>(); /* 用作统计频度, 选择Long类型, 确保不会overflow */
        int size; /* 区间的个数 */
        int bias;
        boolean signed = false;

        Histogram(int size, boolean signed) {
            this(size);
            this.signed = signed;
            this.bias = size / 2;
        }

        Histogram(int size) {
            for (int i = 0; i < size; i++) {
                data.add(0L);
            }
            this.size = size;
        }

        void add(int n) {
            // TODO 处理负数问题
            if (signed && n < size && -n < size) {

                if (n >= bias) {
                    n = n - bias;
                } else {
                    n = n + bias;
                }

                data.set(n, data.get(n) + 1);
            } else if (n < size) {
                data.set(n, data.get(n) + 1);
            }
        }

        int size() {
            return size;
        }

        long getValue(int i) {
            return data.get(i);
        }

        int getIndex(int x) {
            if (signed) {
                return x - bias;
            } else {
                return x;
            }
        }


        /**
         * 寻找 median 所在的区间
         *
         * @param mid 中位数的index
         * @return
         */
        Pair midArgMid(long mid) throws IndexOutOfBoundsException {
            long accum = 0;
            for (int i = 0; i < size(); i++) {
                if (accum + getValue(i) >= mid) {
                    return new Pair(accum, getIndex(i));
                }
                accum = accum + getValue(i);
            }
            throw new IndexOutOfBoundsException();
        }
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
     * 从文件中查找中位数
     * <p>
     * 数据为 long 类型, 利用分层思想, 将分为4个层面实现
     *
     * @param input 数据文件
     * @param count 个数
     */
    public long find(Path input, final long count) throws IOException {

        long mid = count / 2;
        int countOfRange = (int) Math.pow(2, 16);

        /* 第一层 */
        FileInputBuffer buffer0 = new FileInputBuffer(input);
        Histogram firstHistogram = new Histogram(countOfRange);
        while (!buffer0.empty()) {
            long number = buffer0.pop();
            firstHistogram.add(partOfLong(number, 0)); /* first part of a long integer */
        }
        Pair first = firstHistogram.midArgMid(mid);
        buffer0.close();

        /* 第二层 */
        FileInputBuffer buffer1 = new FileInputBuffer(input);
        Histogram secondHistogram = new Histogram(countOfRange);
        while (!buffer1.empty()) {
            long number = buffer1.pop();
            if (partOfLong(number, 0) == first.index) {
                secondHistogram.add(partOfLong(number, 1)); /* second part of a long integer */
            }
        }
        Pair second = secondHistogram.midArgMid((mid - first.count));
        buffer1.close();

        /* 第三层 */
        FileInputBuffer buffer2 = new FileInputBuffer(input);
        Histogram thirdHistogram = new Histogram(countOfRange);
        while (!buffer2.empty()) {
            long number = buffer2.pop();
            /* 结合第一, 二层信息 */
            if (partOfLong(number, 0) == first.index
                    && partOfLong(number, 1) == second.index) {
                thirdHistogram.add(partOfLong(number, 2)); /* third part of a long integer */
            }
        }
        Pair third = thirdHistogram.midArgMid((mid - first.count - second.count));
        buffer2.close();

        /* 第四层 */
        FileInputBuffer buffer3 = new FileInputBuffer(input);
        Histogram fourthHistogram = new Histogram(countOfRange);
        while (!buffer3.empty()) {
            long number = buffer3.pop();
            if (partOfLong(number, 0) == first.index
                    && partOfLong(number, 1) == second.index
                    && partOfLong(number, 2) == third.index) {
                fourthHistogram.add(partOfLong(number, 3)); /* fourth part of a long integer */
            }
        }
        Pair fourth = fourthHistogram.midArgMid((mid - first.count - second.count - third.count));
        buffer3.close();

        /* 综合四个部分. 注意运算符优先级和类型转换 */
        return ((long) first.index << 48)
                + ((long) second.index << 32)
                + ((long) third.index << 16)
                + (long) fourth.index;
    }
}