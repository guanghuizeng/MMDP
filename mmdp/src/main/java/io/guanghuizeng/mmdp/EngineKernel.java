package io.guanghuizeng.mmdp;

import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.fs.VirtualPath;
import io.guanghuizeng.fs.input.VirtualFile;
import io.guanghuizeng.fs.input.VirtualFileInput;
import io.guanghuizeng.fs.input.VirtualFileInputBuffer;
import io.guanghuizeng.fs.output.VirtualFileOutput;
import io.guanghuizeng.fs.output.WritableVirtualFile;
import io.guanghuizeng.mmdp.algs2.Histogram;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *
 */
public class EngineKernel {

    // TODO 远程调用
    // TODO 多线程. 非常重要... 吗?

    private EngineFront front;
    private EngineBackend backend;
    private FileSystem fileSystem;

    public EngineKernel(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
        this.front = new EngineFront(fileSystem);
        this.backend = new EngineBackend(fileSystem);
    }

    /******
     * Median
     *
     * @param spec
     * @return
     * @throws IOException
     */
    public long submit(MedianTaskSpec spec) throws IOException {
        // spec -> length, getInput
        VirtualPath path = spec.getInput();
        long length = spec.getLength();
        long mid = length / 2; // TODO 从path获取文件长度, 再得到中位数所在位置

        /* 第一层 */
        Histogram firstHistogram = execute(path, MedianPhase.FIRST);
        Histogram.Pair first = firstHistogram.midArgMid(mid);

        /* 第二层 */
        Histogram secondHistogram = execute(path, MedianPhase.SECOND, first.index());
        Histogram.Pair second = secondHistogram.midArgMid((mid - first.count()));

        /* 第三层 */
        Histogram thirdHistogram = execute(path, MedianPhase.THIRD, first.index(), second.index());
        Histogram.Pair third = thirdHistogram.midArgMid((mid - first.count() - second.count()));

        /* 第四层 */
        Histogram fourthHistogram = execute(path, MedianPhase.FOURTH, first.index(), second.index(), third.index());
        Histogram.Pair fourth = fourthHistogram.midArgMid((mid - first.count() - second.count() - third.count()));

        // construct
        return ((long) first.index() << 48) + ((long) second.index() << 32)
                + ((long) third.index() << 16) + (long) fourth.index();
    }

    public Histogram execute(VirtualPath path, MedianPhase phase, long... args) throws IOException {

        // main task
        MedianTaskSpec taskSpec = MedianTaskSpec.build(path, phase, args);

        // map
        List<MedianSubTaskSpec> subTaskSpecs = front.map(taskSpec);

        // reduce
        List<Histogram> histogramList = backend.execMedian(subTaskSpecs);

        // merge
        Histogram result = new Histogram();  // <= merge hL.
        result.merge(histogramList);

        return result;
    }

    /**
     * @param spec
     * @return
     */
    public int submit(SortTaskSpec spec) {

        // 1. 分解任务
        // 2. 递交给 backend 执行
        // 3. 合并执行结果, 然后返回

        try {
            List<SortSubTaskSpec> subTaskSpecs = front.map(spec);
            List<Uri> tmps = backend.execute(subTaskSpecs);

            // merge: urls -> vp
            mergeSortedFiles(tmps, spec.getOutput());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public VirtualPath mergeSortedFiles(List<Uri> sortedFiles, VirtualPath output)
            throws Exception {

        PriorityQueue<VirtualFileInputBuffer> queue = new PriorityQueue<>();
        for (Uri uri : sortedFiles) {
            VirtualFile file = fileSystem.newFile(uri);
            VirtualFileInputBuffer buffer = new VirtualFileInputBuffer(
                    new VirtualFileInput(file));
            if (!buffer.isEmpty()) {
                queue.add(buffer);
            }
        }

        WritableVirtualFile outputFile = fileSystem.newWritableFile(output);
        VirtualFileOutput fileOutput = new VirtualFileOutput(outputFile);

        try {
            while (queue.size() > 0) {
                VirtualFileInputBuffer buffer = queue.poll();  // 读取
                fileOutput.writeLong(buffer.pop());            // 写入

                if (buffer.isEmpty()) {                        // 判断
                    buffer.close();
                } else {
                    queue.add(buffer);
                }
            }
        } finally {
            // close the connections
            fileOutput.close();
        }

        return output;
    }

    /**
     * 查找前K项最大值
     */
    public List<Long> submit(MaxTaskSpec spec) throws Exception {

        List<MaxSubTaskSpec> subTaskSpecs = front.map(spec);
        List<Uri> tmps = backend.execMax(subTaskSpecs);

        // reduce
        PriorityQueue<VirtualFileInputBuffer> queue = new PriorityQueue<>(new Comparator<VirtualFileInputBuffer>() {
            @Override
            public int compare(VirtualFileInputBuffer b1, VirtualFileInputBuffer b2) {
                /** reverse order */
                return b2.peek().compareTo(b1.peek());
            }
        });


        for (Uri uri : tmps) {
            VirtualFile file = fileSystem.newFile(uri);
            VirtualFileInputBuffer buffer = new VirtualFileInputBuffer(
                    new VirtualFileInput(file));
            if (!buffer.isEmpty()) {
                queue.add(buffer);
            }
        }

        List<Long> result = new ArrayList<>();
        int count = spec.getCount();
        while (queue.size() > 0 && count > 0) {
            VirtualFileInputBuffer buffer = queue.poll();  // 读取
            result.add(buffer.pop());                      // 写入
            count--;

            if (buffer.isEmpty()) {                        // 判断
                buffer.close();
            } else {
                queue.add(buffer);
            }
        }

        return result;
    }

    /******
     * 存在性判断
     ******/
    public Map<Long, Boolean> submit(ExistTaskSpec spec) throws Exception {

        List<ExistSubTaskSpec> subTaskSpecs = front.map(spec);
        List<Map<Long, Boolean>> subResults = backend.execExist(subTaskSpecs);

        // 合并结果(字典)
        Map<Long, Boolean> result = new HashMap<>();
        for (Map<Long, Boolean> m : subResults) {
            for (Long key : m.keySet()) {
                if (result.containsKey(key)) {
                    boolean value = result.get(key);
                    result.put(key, value || m.get(key));
                } else {
                    result.put(key, m.get(key));
                }
            }
        }

        return result;
    }

}
