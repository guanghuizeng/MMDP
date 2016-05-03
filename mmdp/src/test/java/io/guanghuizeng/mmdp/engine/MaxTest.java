package io.guanghuizeng.mmdp.engine;

import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.fs.ServiceID;
import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.fs.VirtualPath;
import io.guanghuizeng.mmdp.Engine;
import io.guanghuizeng.mmdp.algs2.FileOutputBuffer;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


/**
 * Created by guanghuizeng on 16/5/3.
 */
public class MaxTest {

    @Test
    public void test6() throws Exception {

        /** CAUTION: 该测试对文件系统有前置条件, 不能算是unit test. */

        String input = "/data/data10m64Test";
        Path dir = Paths.get(System.getProperty("user.home"), "/mmdpfs/user0", input);

        Files.deleteIfExists(dir);
        File file = Files.createFile(dir).toFile();
        FileOutputBuffer buffer = new FileOutputBuffer(file.toPath());

        ArrayList<Long> data = new ArrayList<>();

        Random random = new Random();
        int N = 10 * 1000;
        for (long i = 0; i < N; i++) {
            long n = Math.abs(random.nextLong());
            buffer.writeLong(n);
            data.add(n);
        }
        buffer.close();

        /** 排序列表元素, 再取出前 k 位 */
        int k = 100;
        data.sort(Comparator.reverseOrder());
        List<Long> expected = data.subList(0, k);
        expected.sort(Comparator.naturalOrder());

        /** 用 Engine.median 方法计算中位数 */
        VirtualPath in = new VirtualPath(Paths.get(input));
        FileSystem fileSystem = new FileSystem();
        ServiceID service0 = new ServiceID("127.0.0.1", 8070, 8090);
        fileSystem.addServices(service0);
        fileSystem.put(in, new Uri(service0, new VirtualPath(input)));

        Engine engine = new Engine(fileSystem);

        List<Long> result = engine.max(in, k);

        assertEquals(expected, result);

    }
}
