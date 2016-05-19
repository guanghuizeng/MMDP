package io.guanghuizeng.mmdp.engine;

import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.fs.ServiceID;
import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.fs.VirtualPath;
import io.guanghuizeng.mmdp.Engine;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.*;

/**
 *
 */
public class EngineTopTest {

    @Test
    public void test() throws Exception {

        String input = "/data/data1k64";

        VirtualPath in = new VirtualPath(Paths.get(input));

        FileSystem fileSystem = new FileSystem();

        ServiceID service0 = new ServiceID("127.0.0.1", 8070, 8090);
        ServiceID service1 = new ServiceID("127.0.0.1", 8071, 8091);

        fileSystem.addServices(service0, service1);
        fileSystem.put(in, new Uri(service0, new VirtualPath(input)));
        fileSystem.put(in, new Uri(service1, new VirtualPath(input)));

        Engine engine = new Engine(fileSystem);

        Map<Long, Long> result = engine.top(in, 10);
        List<Map.Entry<Long, Long>> list = new LinkedList<>(result.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Long, Long>>() {
            @Override
            public int compare(Map.Entry<Long, Long> o1, Map.Entry<Long, Long> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        for (Map.Entry<Long, Long> e : list) {
            System.out.println("Key: " + e.getKey() + " value: " + e.getValue());
        }

    }
}
