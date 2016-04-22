package io.guanghuizeng.mmdp.engine;

import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.fs.VirtualPath;
import io.guanghuizeng.mmdp.algs2.Histogram;

import java.io.IOException;
import java.nio.file.Path;

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
     * 最顶层
     *
     * @param path
     * @param length
     * @return
     * @throws IOException
     */
    public long median(Path path, long length) throws IOException {

        long mid = length / 2; // TODO 从path获取文件长度, 再得到中位数所在位置

        /* 第一层 */
        Histogram firstHistogram = execute(path, Phase.FIRST);
        Histogram.Pair first = firstHistogram.midArgMid(mid);
        // System.out.printf("first.index() = %d, first.count()() = %d\n", first.index(), first.count());

        /* 第二层 */
        Histogram secondHistogram = execute(path, Phase.SECOND, first.index());
        Histogram.Pair second = secondHistogram.midArgMid((mid - first.count()));
        // System.out.printf("second.index()() = %d,  second.count() = %d\n", second.index(), second.count());

        /* 第三层 */
        Histogram thirdHistogram = execute(path, Phase.THIRD, first.index(), second.index());
        Histogram.Pair third = thirdHistogram.midArgMid((mid - first.count() - second.count()));
        // System.out.printf("third.index() = %d, third.count() = %d\n", third.index(), third.count());

        /* 第四层 */
        Histogram fourthHistogram = execute(path, Phase.FOURTH, first.index(), second.index(), third.index());
        Histogram.Pair fourth = fourthHistogram.midArgMid((mid - first.count() - second.count() - third.count()));
        // System.out.printf("fourth.index() = %d, fourth.count() = %d\n", fourth.index(), fourth.count());

        // combine
        return ((long) first.index() << 48) + ((long) second.index() << 32)
                + ((long) third.index() << 16) + (long) fourth.index();
    }

    public Histogram execute(Path path, Phase phase, long... args) throws IOException {

        MedianTaskSpec spec = MedianTaskSpec.build(path, phase, args);
        Histogram result = kernel.submit(spec);

        return result;
    }

    /****************
     * sort
     ****************/

    public void sort(VirtualPath input, VirtualPath output) {
        SortTaskSpec spec = SortTaskSpec.build(input, output);
        int result = kernel.submit(spec);
    }
}
