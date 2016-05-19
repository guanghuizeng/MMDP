package io.guanghuizeng.mmdp.algs2;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 *
 */
public class Top {

    /**
     * 假设: 数据分布情况未知
     * <p>
     * 步骤:
     * 1. 可用内存 -> 可容纳Long类型对象个数的上限, 记为N -> 结合low和up, 划分range
     * 2. 扫描一遍数据, 统计每个range中包含的数据个数
     * 3. 合并数据量小的range, 以N为上限
     * 4. 扫描一遍数据, 分布到相应的range, 再写入到文件
     * 5. 分别筛选出每个文件的Top-k
     * 6. 合并结果, 返回Top-k
     */

    private class Range {
        private long low;
        private long up;
        private long count;

        public Range(long up, long low) {
            this.low = low;
            this.up = up;
        }

        public Range(long low, long up, long count) {
            this.low = low;
            this.up = up;
            this.count = count;
        }

        public void increaseCount() {
            count++;
        }

        public long length() {
            return up - low;
        }

        public void setLow(long low) {
            this.low = low;
        }

        public void setUp(long up) {
            this.up = up;
        }

        public void setCount(long count) {
            this.count = count;
        }


    }

    private List<Range> ranges = new ArrayList<>();
    private int BYTES = Long.BYTES;
    private Path input;

    public Map<Long, Long> top(Path input, long up, long low, int k) throws IOException {

        // 第一步
        long maxOfTmpFiles = 1024;
        long size = bestSizeOfBlock(input.toFile().length(), maxOfTmpFiles);
        long length = size / BYTES; // 即区间长度.
        init(up, low, length);

        // 第二步
        count(input);

        // 第三步
        merge(length);

        // 第四步
        List<Path> subFiles = dispatch(input);
        List<Map<Long, Long>> subResults = new ArrayList<>();
        for (Path p : subFiles) {
            subResults.add(top(p, k));
        }

        // 第五步
        Map<Long, Long> result = merge(subResults, k);
        return result;
    }

    /**
     * 初始化 ranges. 如果数值空间太大, 那么range会撑爆内存.
     *
     * @param low    下限
     * @param up     上限
     * @param length 区间宽度
     */
    private void init(long up, long low, long length) {
        for (long i = low; i < up; i = i + length) {
            if ((i + length) <= up) {
                ranges.add(new Range(i, i + length));
            } else {
                ranges.add(new Range(i, up));
            }
        }
    }

    /**
     * 统计每个区间中的元素个数
     *
     * @param input
     */
    private void count(Path input) throws IOException {
        FileInputBuffer buffer = new FileInputBuffer(input);
        while (!buffer.empty()) {
            long value = buffer.pop();
            ranges.get(index(value)).increaseCount();
        }
    }

    /**
     * 合并区间. why? 减小IO次数, 提高性能.
     *
     * @param max
     */
    private void merge(long max) {
        List<Range> newRanges = new ArrayList<>();
        Range tmp = new Range(0, 0, 0);
        for (Range r : ranges) {
            if ((tmp.count + r.count) <= max) {
                tmp.setUp(r.up);
                tmp.setCount(tmp.count + r.count);
            } else {
                newRanges.add(tmp);
                tmp = r;
            }
        }
        if (tmp.count != 0) {
            newRanges.add(tmp);
        }
        ranges = newRanges;
    }

    private List<Path> dispatch(Path input) throws IOException {
        // 1. 为每个range创建文件.
        // 2. 遍历 input, 根据元素值的大小, 判断所属range, 再写入到对应文件.
        List<Path> result = new ArrayList<>();
        List<FileOutputBuffer> buffers = new ArrayList<>();
        for (Range r : ranges) {
            Path tmp = Paths.get(input.toString().concat(String.valueOf(System.currentTimeMillis())));
            result.add(tmp);
            buffers.add(new FileOutputBuffer(tmp));
        }

        FileInputBuffer inputBuffer = new FileInputBuffer(input);
        try {
            while (!inputBuffer.empty()) {
                Long value = inputBuffer.pop();
                buffers.get(index(value)).writeLong(value);
            }
        } finally {
            inputBuffer.close();
            for (FileOutputBuffer b : buffers) {
                b.close();
            }
        }
        return result;
    }

    /**
     * 根据x的值, 判断其所属的range
     *
     * @param x 元素
     * @return 所属range在ranges中的index.
     */
    private int index(long x) {
        for (int i = 0; i < ranges.size(); i++) {
            Range r = ranges.get(i);
            if (x >= r.low && x < r.up) {
                return i;
            }
        }
        return ranges.size() - 1;
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
    private static long bestSizeOfBlock(long sizeOfFile, long maxOfTmpFiles) {
        long maxOfMemory = availableMemory();
        long blockSize = sizeOfFile / maxOfTmpFiles + (sizeOfFile % maxOfTmpFiles == 0 ? 0 : 1);
        /* 尽量多读入一些数据, 在内存中排好序  */
        if (blockSize <= maxOfMemory / 2) {
            blockSize = maxOfMemory / 2;
        }
        return blockSize;
    }

    public static Map<Long, Long> search(Path input, long up, long low, int k) throws IOException {
        Top top = new Top();
        System.out.printf("input: %s, up: %d, low: %d, k: %d\n", input, up, low, k);
        return top.top(input, up, low, k);
    }
}
