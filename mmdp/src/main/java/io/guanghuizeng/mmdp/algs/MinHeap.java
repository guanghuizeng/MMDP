package io.guanghuizeng.mmdp.algs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guanghuizeng on 16/3/18.
 */
public class MinHeap {

    private List<Long> data;
    private int lastIndex = -1;

    public MinHeap(int capacity) {
        data = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            data.add(Long.MAX_VALUE);
        }
    }

    private int parent(int i) {
        return i / 2;
    }

    private int left(int i) {
        return 2 * i;
    }

    private int right(int i) {
        return 2 * i + 1;
    }

    private void exchange(int i, int j) {
        long tmp = data.get(i);
        data.set(i, data.get(j));
        data.set(j, tmp);
    }

    private void decreaseKey(int i, long key) throws IOException {
        if (key > data.get(i)) {
            throw new IOException("New key is bigger than current key");
        }

        data.set(i, key);
        while (i > 0 && data.get(parent(i)) > data.get(i)) {
            exchange(i, parent(i));
            i = parent(i);
        }
    }

    public void insert(long x) throws IOException {
        lastIndex = lastIndex + 1;
        data.set(lastIndex, Long.MAX_VALUE);
        decreaseKey(lastIndex, x);
    }

    private void heapify(int i) {
        int l = left(i);
        int r = right(i);
        int smallest = i;

        if (l <= lastIndex && data.get(l) < data.get(i)) {
            smallest = l;
        }

        if (r <= lastIndex && data.get(r) < data.get(smallest)) {
            smallest = r;
        }
        if (smallest != i) {
            exchange(i, smallest);
            heapify(smallest);
        }
    }

    public long extract() throws IOException {
        if (lastIndex >= 0) {
            long min = min();
            data.set(0, data.get(lastIndex));
            lastIndex = lastIndex - 1;
            heapify(0);
            return min;
        } else {
            throw new IOException("The heap is empty");
        }
    }

    public long min() throws IOException {
        if (lastIndex >= 0) {
            return data.get(0);
        } else {
            throw new IOException("The heap is empty");
        }
    }

    public int getHeapSize() {
        return lastIndex + 1;
    }

    /**
     * 检查是否符合heap性质
     */
    public boolean check() {

        boolean test = true;
        for (int i = 0; i < lastIndex / 2; i++) {
            if (data.get(i) > data.get(left(i)) || data.get(i) > data.get(right(i))) {
                test = false;
                break;
            }
        }
        return test;

    }
}