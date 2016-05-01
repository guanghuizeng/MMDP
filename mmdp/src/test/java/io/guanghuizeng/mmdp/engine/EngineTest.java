package io.guanghuizeng.mmdp.engine;

import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.fs.ServiceID;
import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.fs.VirtualPath;
import io.guanghuizeng.mmdp.Engine;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * Created by guanghuizeng on 16/4/22.
 */
public class EngineTest {

    @Test
    public void test() {

        String input = "/data/data10m64";
        String output = "/data/data10m64Sorted";

        VirtualPath in = new VirtualPath(Paths.get(input));
        VirtualPath out = new VirtualPath(Paths.get(output));

        FileSystem fileSystem = new FileSystem();

        ServiceID service0 = new ServiceID("127.0.0.1", 8070, 8090);
        ServiceID service1 =  new ServiceID("127.0.0.1", 8071, 8091);

        fileSystem.addServices(service0,service1);
        fileSystem.put(in, new Uri(service0, new VirtualPath(input)));
        fileSystem.put(in, new Uri(service1, new VirtualPath(input)));

        Engine engine = new Engine(fileSystem);
        engine.sort(in, out);
    }

    @Test
    public void t() {

        String host = "127.0.0.1";
        int syncPort = 8071;
        int enginePort = 8091;

        int code = ((Integer.parseInt(host.replaceAll("[.]", "")) & 0xFFF) << 4)
                + ((syncPort & 0xFFF) << 2) + (enginePort & 0xFFF);

        System.out.println(code);
        System.out.println(syncPort & 0xFF);
    }
}
