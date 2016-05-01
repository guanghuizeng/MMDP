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
import java.util.List;
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
    public Histogram submit(MedianTaskSpec spec) throws IOException {

        return backend.execute(spec);
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


}
