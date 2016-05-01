package io.guanghuizeng.mmdp;

import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.mmdp.algs2.ExternalSort;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Comparator;

/**
 * 实现服务器端的业务逻辑, 面向 sub task 设计.
 * TODO 线程安全
 */
public class EngineBackendExecutor {

    private FileSystem fileSystem  = new FileSystem();

    public SortSubTaskSpec exec(SortSubTaskSpec spec) throws IOException {

        ExternalSort.sort(
                Paths.get(fileSystem.getHome(spec.getInput().getServiceID().code()),
                        spec.getInput().getActualPath().getLocalPath().toString()),
                Paths.get(fileSystem.getHome(spec.getOutput().getServiceID().code()),
                        spec.getOutput().getActualPath().getLocalPath().toString()),
                Comparator.naturalOrder());

        return spec;
    }
}