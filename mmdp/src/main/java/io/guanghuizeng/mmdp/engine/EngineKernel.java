package io.guanghuizeng.mmdp.engine;

import io.guanghuizeng.fs.*;
import io.guanghuizeng.fs.input.VirtualFile;
import io.guanghuizeng.fs.input.VirtualFileInput;
import io.guanghuizeng.fs.input.VirtualFileInputBuffer;
import io.guanghuizeng.fs.output.VirtualFileOutput;
import io.guanghuizeng.fs.output.WritableVirtualFile;
import io.guanghuizeng.mmdp.algs2.Histogram;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.Future;

/**
 * Created by guanghuizeng on 16/4/21.
 */
public class EngineKernel {

    // TODO 远程调用
    // TODO 多线程

    private EngineFront front = new EngineFront();
    EngineBackend backend = new EngineBackend();

    public Histogram submit(MedianTaskSpec spec) throws IOException {

        return backend.execute(spec);
    }

    public int submit(SortTaskSpec spec) {

        // 1. 分解任务
        // 2. 递交给 backend 执行
        // 3. 合并执行结果, 然后返回

        try {
            List<SortSubTaskSpec> subTaskSpecs = front.map(spec);
            List<VirtualUrl> results = backend.execute(subTaskSpecs);

            // merge: urls -> vp
            mergeSortedFiles(results, spec.getOutput());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public VirtualPath mergeSortedFiles(List<VirtualUrl> sortedFiles, VirtualPath output) {



        return output;
    }


}
