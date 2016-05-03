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

    @Test
    public void test7() throws Exception {

        /** CAUTION: 该测试对文件系统有前置条件, 不能算是unit test. */

        ArrayList<Long> data = new ArrayList<>();
        int N = 10 * 1000 * 1000;
        Random random = new Random();

        // file 1

        String input = "/data/data10m64Test";
        Path dir = Paths.get(System.getProperty("user.home"), "/mmdpfs/user0", input);

        Files.deleteIfExists(dir);
        File file = Files.createFile(dir).toFile();
        FileOutputBuffer buffer = new FileOutputBuffer(file.toPath());

        for (long i = 0; i < N; i++) {
            long n = Math.abs(random.nextLong());
            buffer.writeLong(n);
            data.add(n);
        }
        buffer.close();

        // file 2
        String input1 = "/data/data10m64Test";
        Path dir1 = Paths.get(System.getProperty("user.home"), "/mmdpfs/user1", input);

        Files.deleteIfExists(dir1);
        File file1 = Files.createFile(dir1).toFile();
        FileOutputBuffer buffer1 = new FileOutputBuffer(file1.toPath());

        for (long i = N; i < 2 * N; i++) {
            long n = Math.abs(random.nextLong());
            buffer1.writeLong(n);
            data.add(n);
        }
        buffer1.close();

        /** 排序列表元素, 再取出前 k 位 */
        int k = 100;
        data.sort(Comparator.reverseOrder());
        List<Long> expected = data.subList(0, k);

        /** 用 Engine.median 方法计算中位数 */
        VirtualPath in = new VirtualPath(Paths.get(input));

        FileSystem fileSystem = new FileSystem();
        ServiceID service0 = new ServiceID("127.0.0.1", 8070, 8090);
        ServiceID service1 = new ServiceID("127.0.0.1", 8071, 8091);

        fileSystem.addServices(service0);
        fileSystem.addServices(service1);

        fileSystem.put(in, new Uri(service0, new VirtualPath(input)));
        fileSystem.put(in, new Uri(service1, new VirtualPath(input1)));


        Engine engine = new Engine(fileSystem);
        List<Long> result = engine.max(in, k);

        assertEquals(expected, result);

    }
}
