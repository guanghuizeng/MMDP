package io.guanghuizeng.mmdp.algs2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by guanghuizeng on 16/4/21.
 */
public class Histogram {

    private List<Long> data = new ArrayList<>(); /* 用作统计频度, 选择Long类型, 确保不会overflow */
    private int size; /* 区间的个数 */
    private int bias;
    private boolean signed = false;

    public class Pair {
        private long count = 0;  /* 频度 */
        private int index = 0;   /* 区间 */

        Pair(long count, int index) {
            this.count = count;
            this.index = index;
        }

        public long count() {
            return count;
        }

        public int index() {
            return index;
        }
    }

    public Histogram() {

    }

    public Histogram(int size, boolean signed) {
        this(size);
        this.signed = signed;
        this.bias = size / 2;
    }

    public Histogram(int size) {
        for (int i = 0; i < size; i++) {
            data.add(0L);
        }
        this.size = size;
    }

    public void add(int n) {
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

    public int size() {
        return size;
    }

    public long getValue(int i) {
        return data.get(i);
    }

    public int getIndex(int x) {
        if (signed) {
            return x - bias;
        } else {
            return x;
        }
    }

    public List<Long> toList() {
        List<Long> result = new ArrayList<>();
        result.addAll(data);
        return result;
    }


    /**
     * 寻找 median 所在的区间
     *
     * @param mid 中位数的index
     * @return
     */
    public Pair midArgMid(long mid) throws IndexOutOfBoundsException {
        long accum = 0;
        for (int i = 0; i < size(); i++) {
            if (accum + getValue(i) >= mid) {
                return new Pair(accum, getIndex(i));
            }
            accum = accum + getValue(i);
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * 合并多个 histograms, 生成并返回新的对象
     *
     * @param histograms
     * @return
     */
    public void merge(Histogram... histograms) {
        // todo 合并多个列表

        List<List<Long>> all = new LinkedList<>();
        for (Histogram h : histograms) {
            all.add(h.toList());
        }

        while (!all.isEmpty()) {


        }
    }
}