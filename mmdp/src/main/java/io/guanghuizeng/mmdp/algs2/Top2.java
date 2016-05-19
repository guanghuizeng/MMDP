package io.guanghuizeng.mmdp.algs2;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * 在文件中找出出现次数最多的前K位数.
 */
public class Top2 {

    private class Range {
        private Long low;
        private Long up;
        private Path path;
        private int k;
        private Map<Long, Long> topk;

        public long length() {
            return this.path.toFile().length();
        }

        public Range(long low, long up, Path path, int k) {
            this.low = low;
            this.up = up;
            this.path = path;
            this.k = k;
        }
    }

    // sorted
    private List<Range> ranges = new LinkedList<>();
    private long availableMemory = 0;
    private int k;

    public Map<Long, Long> top(Path input, long up, long low, int k) throws IOException {
        this.k = k;
        this.availableMemory = availableMemory();
        // 第一步
        Range initial = new Range(low, up, input, k);
        divide(initial);
        // 第二步
        conquer();
        // 第三步
        return merge();
    }

    // range -> ranges
    private void divide(Range range) throws IOException {
        PriorityQueue<Range> todo = new PriorityQueue<>(new Comparator<Range>() {
            @Override
            public int compare(Range o1, Range o2) {
                return o1.low.compareTo(o2.low);
            }
        });
        todo.add(range);
        List<Range> done = new LinkedList<>();
        while (!todo.isEmpty()) {
            List<Range> t = dispatch(todo.poll());
            for (Range r : t) {
                // 判断是否需要进一步分割
                if (r.length() > availableMemory) {
                    todo.add(r);
                } else {
                    done.add(r);
                }
            }
        }
        ranges = done;
    }

    private List<Range> dispatch(Range r) throws IOException {
        // 0. 分解range
        // r -> list of range

        List<Range> rangeList = new LinkedList<>();

        long low = r.low;
        long up = r.up;
        long count = (r.length() / availableMemory) + 1;
        long length = ((BigInteger.valueOf(up).subtract(BigInteger.valueOf(low)).longValue() / count));

        for (long i = low; i < up; i = i + length) {
            if ((i + length) <= up) {
                rangeList.add(new Range(i, i + length,
                        Paths.get(r.path.toString().concat(String.valueOf(System.currentTimeMillis()))),
                        r.k));
            } else {
                rangeList.add(new Range(i, up,
                        Paths.get(r.path.toString().concat(String.valueOf(System.currentTimeMillis()))),
                        r.k));
            }
        }
        return dispatch(r, rangeList);
    }

    private List<Range> dispatch(Range input, List<Range> outRanges) throws IOException {
        // 1. 为每个range创建文件.
        // 2. 遍历 input, 根据元素值的大小, 判断所属range, 再写入到对应文件.
        // List<Path> result = new ArrayList<>();
        List<FileOutputBuffer> buffers = new ArrayList<>();
        for (Range r : outRanges) {
            buffers.add(new FileOutputBuffer(r.path));
        }
        FileInputBuffer inputBuffer = new FileInputBuffer(input.path);
        try {
            while (!inputBuffer.empty()) {
                Long value = inputBuffer.pop();
                buffers.get(index(value, outRanges)).writeLong(value);
            }
        } finally {
            inputBuffer.close();
            for (FileOutputBuffer b : buffers) {
                b.close();
            }
        }
        return outRanges;
    }

    /**
     * 根据x的值, 判断其所属的range
     *
     * @param x      元素
     * @param ranges 一个range列表
     * @return 所属range在ranges中的index.
     */
    private int index(long x, List<Range> ranges) {
        for (int i = 0; i < ranges.size(); i++) {
            Range r = ranges.get(i);
            if (x >= r.low && x < r.up) {
                return i;
            }
        }
        return ranges.size() - 1;
    }

    /**
     * 分别处理每个文件
     *
     * @throws IOException
     */
    private void conquer() throws IOException {
        for (Range r : ranges) {
            r.topk = top(r.path, r.k);
        }
    }

    /**
     * 统计文件中元素的频率, 要求文件可读进内存
     *
     * @param input
     * @param k
     * @return
     */
    private Map<Long, Long> top(Path input, int k) throws IOException {
        FileInputBuffer buffer = new FileInputBuffer(input);
        TreeMap<Long, Long> count = new TreeMap<>();
        try {
            while (!buffer.empty()) {
                long value = buffer.pop();
                if (count.containsKey(value)) {
                    Long origin = count.get(value);
                    count.put(value, ++origin);
                } else {
                    count.put(value, 1L);
                }
            }
        } finally {
            buffer.close();
        }

        // 按value排序map, 筛选前k项.
        PriorityQueue<Map.Entry<Long, Long>> queue = new PriorityQueue<>(new Comparator<Map.Entry<Long, Long>>() {
            @Override
            public int compare(Map.Entry<Long, Long> o1, Map.Entry<Long, Long> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        int size = 0;
        for (Map.Entry<Long, Long> entry : count.entrySet()) {
            if (size < k) {
                queue.add(entry);
                size++;
            } else {
                if (entry.getValue().compareTo(queue.peek().getValue()) > 0) {
                    queue.poll();
                    queue.add(entry);
                }
            }
        }

        TreeMap<Long, Long> result = new TreeMap<>();
        int size1 = queue.size();
        for (int i = 0; i < size1; i++) {
            Map.Entry<Long, Long> entry = queue.poll();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    private Map<Long, Long> merge() {
        List<Map<Long, Long>> interResults = new LinkedList<>();
        for (Range r : ranges) {
            interResults.add(r.topk);
        }
        return merge(interResults, k);
    }

    /**
     * 计算可用的内存大小, 以byte为单位
     *
     * @return 返回可用的内存大小, 以byte为单位
     */
    private static long availableMemory() {
        System.gc();
        return Runtime.getRuntime().freeMemory();
    }

    /**
     * 合并不同文件的统计结果, 取出目标项(前k项)
     *
     * @param input
     * @param k
     * @return
     */
    private Map<Long, Long> merge(List<Map<Long, Long>> input, int k) {
        PriorityQueue<Map.Entry<Long, Long>> queue = new PriorityQueue<>(new Comparator<Map.Entry<Long, Long>>() {
            @Override
            public int compare(Map.Entry<Long, Long> o1, Map.Entry<Long, Long> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        int size = 0;
        for (Map<Long, Long> count : input) {
            for (Map.Entry<Long, Long> entry : count.entrySet()) {
                if (size <= k) {
                    queue.add(entry);
                    size++;
                } else {
                    if (entry.getValue().compareTo(queue.peek().getValue()) > 0) {
                        queue.poll();
                        queue.add(entry);
                    }
                }
            }
        }
        TreeMap<Long, Long> result = new TreeMap<>();
        int size1 = queue.size();
        for (int i = 0; i < size1; i++) {
            Map.Entry<Long, Long> entry = queue.poll();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static Map<Long, Long> search(Path input, long up, long low, int k) throws IOException {
        Top2 top = new Top2();
        return top.top(input, up, low, k);
    }
}
