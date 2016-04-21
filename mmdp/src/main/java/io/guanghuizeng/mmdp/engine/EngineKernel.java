package io.guanghuizeng.mmdp.engine;

import io.guanghuizeng.mmdp.algs2.Histogram;

import java.io.IOException;

/**
 * Created by guanghuizeng on 16/4/21.
 */
public class EngineKernel {

    // TODO 远程调用
    // TODO 多线程

    public Histogram submit(MedianTaskSpec spec) throws IOException {
        EngineBackend backend = new EngineBackend();
        return backend.execute(spec);
    }




}
