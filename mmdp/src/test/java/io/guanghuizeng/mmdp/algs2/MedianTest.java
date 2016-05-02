package io.guanghuizeng.mmdp.algs2;

import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.fs.ServiceID;
import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.fs.VirtualPath;
import io.guanghuizeng.mmdp.Engine;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;


public class MedianTest {

    @Test
    public void test3() throws IOException {

        File file = File.createTempFile("median", "test");
        file.deleteOnExit();
        FileOutputBuffer buffer = new FileOutputBuffer(file.toPath());

        ArrayList<Long> data = new ArrayList<>();

        Random random = new Random();
        int N = 10000;
        for (long i = 0; i < N; i++) {
            long n = Math.abs(random.nextLong());
            buffer.writeLong(n);
            data.add(n);
        }
        buffer.close();

        data.sort(Comparator.naturalOrder());
        Long expected = data.get((N - 1) / 2);

        MedianFinder64 finder64 = new MedianFinder64();
        long result = finder64.find(file.toPath(), N);

        assertEquals(Long.toUnsignedString(expected), Long.toUnsignedString(result));
    }

    @Test
    public void test31() throws IOException {
        for (int i = 0; i < 20; i++) {
            test3();
        }
    }
    
    public void test6() throws IOException {

        /** CAUTION: 该测试对文件系统有前置条件, 不能算是unit test. */

        String input = "/data/data10m64Test";
        Path dir = Paths.get(System.getProperty("user.home"), "/mmdpfs/user0", input);

        File file = Files.createFile(dir).toFile();
        file.deleteOnExit();
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

        /** 排序列表元素, 再取出中位数 */
        data.sort(Comparator.naturalOrder());
        Long expected = data.get((N - 1) / 2);

        /** 用 Engine.median 方法计算中位数 */
        VirtualPath in = new VirtualPath(Paths.get(input));
        FileSystem fileSystem = new FileSystem();
        ServiceID service0 = new ServiceID("127.0.0.1", 8070, 8090);
        fileSystem.addServices(service0);
        fileSystem.put(in, new Uri(service0, new VirtualPath(input)));

        Engine engine = new Engine(fileSystem);
        Long result = engine.median(in, N);

        assertEquals(expected, result);
    }
}
