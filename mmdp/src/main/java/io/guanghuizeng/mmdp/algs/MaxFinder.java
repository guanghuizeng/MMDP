package io.guanghuizeng.mmdp.algs;

import io.guanghuizeng.mmdp.utils.FileInputBuffer;
import io.guanghuizeng.mmdp.utils.FileOutputBuffer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by guanghuizeng on 16/3/18.
 */
public class MaxFinder {

    /**
     * 从文件中找出前k个最大数
     *
     * @param input 输入文件
     * @param N     总数量
     * @param k     前k个数
     * @return
     * @throws Exception
     */
    public static List<Long> find(File input, int N, int k) throws IOException {

        FileInputBuffer buffer = new FileInputBuffer(input);
        MinHeap heap = new MinHeap(k);
        for (int i = 0; i < N; i++) {
            long value = buffer.pop();
            if (heap.getHeapSize() < k) {
                heap.insert(value);
            } else {
                if (value > heap.min()) {
                    heap.extract();
                    heap.insert(value);
                }
            }
        }
        buffer.close();

        List<Long> result = new ArrayList<>(k);
        for (int i = 0; i < k; i++) {
            result.add(heap.extract());
        }
        result.sort(Comparator.reverseOrder());

        return result;
    }

    /**
     * 从文件中找出前k个最大数
     *
     * @param input 输入文件
     * @param k     前k个数
     * @return
     * @throws Exception
     */
    public static List<Long> find(File input, int k) throws IOException {

        FileInputBuffer buffer = new FileInputBuffer(input);
        MinHeap heap = new MinHeap(k);

        while (!buffer.empty()) {
            long value = buffer.pop();
            if (heap.getHeapSize() < k) {
                heap.insert(value);
            } else {
                if (value > heap.min()) {
                    heap.extract();
                    heap.insert(value);
                }
            }
        }
        buffer.close();

        List<Long> result = new ArrayList<>(k);
        for (int i = 0; i < k; i++) {
            result.add(heap.extract());
        }
        result.sort(Comparator.reverseOrder());

        return result;
    }

    public static File find(File source, File target, int k) throws IOException {
        List<Long> result = find(source, k);
        FileOutputBuffer buffer = new FileOutputBuffer(target);
        for (long x : result) {
            buffer.writeLong(x);
        }
        buffer.close();
        return target;
    }
}
