package io.guanghuizeng.mmdp.algs;

import io.guanghuizeng.mmdp.utils.ObjectInputBuffer;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by guanghuizeng on 16/3/20.
 */
public class Statistics {

    private Map<Long, Integer> counter = new HashMap<>();

    private void increase(long x) {
        if (counter.containsKey(x)) {
            int value = counter.get(x);
            counter.put(x, value + 1);
        } else {
            counter.put(x, 1);
        }
    }

    /**
     * @param data
     * @return
     * @throws IOException
     */
    public Map<Long, Integer> frequency(File data) throws IOException {
        ObjectInputBuffer buffer = new ObjectInputBuffer(data);
        while (!buffer.empty()) {
            increase(buffer.pop());
        }
        sortByValue(reverseComparator);
        return counter;
    }

    public Map<Long, Integer> frequency(List<Long> data) {
        for (long x : data) {
            increase(x);
        }
        sortByValue(reverseComparator);
        return counter;
    }


    /**
     * 按照value值排序
     */
    private void sortByValue(Comparator<Integer> cmp) {
        //
    }

    public void clear() {
        counter = new HashMap<>();
    }

    public static Comparator<Integer> reverseComparator = new Comparator<Integer>() {
        @Override
        public int compare(Integer r1, Integer r2) {
            return r2.compareTo(r1);
        }
    };
}

