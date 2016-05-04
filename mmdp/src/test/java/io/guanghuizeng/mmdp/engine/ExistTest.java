package io.guanghuizeng.mmdp.engine;

import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.fs.ServiceID;
import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.fs.VirtualPath;
import io.guanghuizeng.fs.output.VirtualFileOutput;
import io.guanghuizeng.fs.output.WritableVirtualFile;
import io.guanghuizeng.mmdp.Engine;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.*;

/**
 * Created by guanghuizeng on 16/5/4.
 */
public class ExistTest {

    /**
     * 测试 Engine.exist 函数
     */
    @Test
    public void test1() throws Exception {

        /** 生成数据 */

        int N = 1000;

        String input = "/data/data1h64Test";
        VirtualPath in = new VirtualPath(Paths.get(input));

        FileSystem fileSystem = new FileSystem();
        ServiceID service0 = new ServiceID("127.0.0.1", 8070, 8090);
        ServiceID service1 = new ServiceID("127.0.0.1", 8071, 8091);
        fileSystem.addServices(service0);
        fileSystem.addServices(service1);
        Uri uri = new Uri(service0, new VirtualPath(input));
        Uri uri1 = new Uri(service1, new VirtualPath(input));
        fileSystem.put(in, uri);
        fileSystem.put(in, uri1);

        // 向第一个文件写入 0 ~ N-1
        WritableVirtualFile file = fileSystem.newWritableFile(uri);
        VirtualFileOutput fileOutput = new VirtualFileOutput(file);
        for (long i = 0; i < N; i++) {
            fileOutput.writeLong(i);
        }
        fileOutput.close();

        // 向第二个文件写入 N ~ 2*N-1
        WritableVirtualFile file1 = fileSystem.newWritableFile(uri1);
        VirtualFileOutput fileOutput1 = new VirtualFileOutput(file1);
        for (long i = N; i < 2 * N; i++) {
            fileOutput1.writeLong(i);
        }
        fileOutput1.close();

        /** 生成检验数据 */
        Set<Long> testData = new HashSet<>();
        testData.add(1024L);
        testData.add(1054L);
        testData.add(24L);
        testData.add(100923L);
        testData.add(10243803L);
        testData.add(1003L);
        testData.add(10203L);
        testData.add(104303L);
        testData.add(102433L);
        testData.add(24380L);
        testData.add(1024803L);
        testData.add(243803L);

        /** 用 Engine.exist 方法判断存在性 */

        Engine engine = new Engine(fileSystem);
        double fpp = 0.03;
        Map<Long, Boolean> existence = engine.exist(in, testData, fpp);

        for (Map.Entry<Long, Boolean> e : existence.entrySet()) {
            System.out.printf("%d, %b\n", e.getKey(), e.getValue());
        }

        /** TODO 统计 */

    }
}
