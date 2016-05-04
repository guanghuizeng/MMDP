package io.guanghuizeng.mmdp;

import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.fs.VirtualPath;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    /****************
     * max
     ****************/
    /**
     * 取出文件中前k个最大值
     *
     * @param input
     * @param k
     * @return 返回结果以 naturalOrder 排序
     * @throws Exception
     */
    public List<Long> max(VirtualPath input, int k) throws Exception {
        MaxTaskSpec spec = MaxTaskSpec.build(input, k);
        return kernel.submit(spec);
    }

    /****************
     * exist
     ****************/
    /**
     * 判断文件中是否包含输入值, 基于概率.
     *
     * @param input 输入文件
     * @param data  需要判断存在的数据
     * @param fpp   false positive probability, 统计学概念.
     * @return key为要检查的数据, value为对应的结果
     */
    public Map<Long, Boolean> exist(VirtualPath input, Set<Long> data, double fpp) throws Exception {
        ExistTaskSpec spec = ExistTaskSpec.build(input, data, fpp);
        return kernel.submit(spec);
    }
}
