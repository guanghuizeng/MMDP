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

    public Histogram(List<Long> data, int size, int bias, boolean signed) {
        this.data = data;
        this.size = size;
        this.bias = bias;
        this.signed = signed;
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

    public int getSize() {
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

    public int getBias() {
        return bias;
    }

    public boolean isSigned() {
        return signed;
    }

    public List<Long> toList() {
        List<Long> result = new ArrayList<>();
        result.addAll(data);
        return result;
    }


    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null) return false;
        if (getClass() != that.getClass()) return false;
        if (!(that instanceof Histogram)) return false;
        return data.equals(((Histogram) that).data) &&
                size == ((Histogram) that).size &&
                bias == ((Histogram) that).bias &&
                signed == ((Histogram) that).signed;
    }

    /**
     * 寻找 median 所在的区间
     *
     * @param mid 中位数的index
     * @return
     */
    public Pair midArgMid(long mid) throws IndexOutOfBoundsException {
        long accum = 0;
        for (int i = 0; i < getSize(); i++) {
            if (accum + getValue(i) >= mid) {
                return new Pair(accum, getIndex(i));
            }
            accum = accum + getValue(i);
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * 合并多个 histograms
     *
     * @param histograms
     * @return
     */
    public void merge(List<Histogram> histograms) {

        List<List<Long>> data = new ArrayList<>();
        data.add(this.toList());
        for (Histogram h : histograms) {
            data.add(h.toList());

            if (h.getSize() > size) {
                size = h.getSize();
            }

            bias = h.getBias();
            signed = h.isSigned();
        }

        List<Long> result = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            long sum = 0;
            List<Integer> remove = new ArrayList<>();
            for (int j = 0; j < data.size(); j++) {
                List<Long> item = data.get(j);
                if (item.size() > i) {
                    sum += item.get(i);
                } else {
                    remove.add(j);
                }
            }

            result.add(sum);

            for (int index : remove) {
                data.remove(index);
            }

        }


        this.data = result;
    }
}