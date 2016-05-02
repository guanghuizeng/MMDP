package io.guanghuizeng.mmdp;

import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.fs.VirtualPath;

import java.io.IOException;

/**
 * Created by guanghuizeng on 16/4/21.
 */
public class Engine {

    private EngineKernel kernel;
    private FileSystem fileSystem;

    public Engine(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
        this.kernel = new EngineKernel(fileSystem);
    }

    /****************
     * median
     ****************/

    /**
     * @param path
     * @param length
     * @return
     * @throws IOException
     */
    public long median(VirtualPath path, long length) throws IOException {
        MedianTaskSpec spec = MedianTaskSpec.build(path, length);
        return kernel.submit(spec);
    }

    /****************
     * sort
     ****************/

    public void sort(VirtualPath input, VirtualPath output) {
        SortTaskSpec spec = SortTaskSpec.build(input, output);
        int result = kernel.submit(spec);
    }
}
