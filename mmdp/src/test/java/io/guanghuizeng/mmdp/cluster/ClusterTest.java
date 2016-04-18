package io.guanghuizeng.mmdp.cluster;

import io.guanghuizeng.fs.Address;
import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.mmdp.Cluster;
import org.junit.Test;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by guanghuizeng on 16/4/4.
 */
public class ClusterTest {


    @Test
    public void testSort() throws Exception {


        // File System

        String host1 = "127.0.0.1";
        int port1 = 8070;
        String host2 = "127.0.0.1";
        int port2 = 8071;

        List<Address> addressList = new ArrayList<>();
        Address a1 = new Address(host1, port1);
        Address a2 = new Address(host2, port2);
        addressList.add(a1);
        addressList.add(a2);

        FileSystem fileSystem = new FileSystem(addressList);

        // mmdp server

        String HOST = System.getProperty("host", "127.0.0.1");
        int PORT1 = Integer.parseInt(System.getProperty("port", "8090"));
        int PORT2 = Integer.parseInt(System.getProperty("port", "8091"));

        Cluster cluster = new Cluster(fileSystem);

        cluster.addNode(HOST, PORT1);
        cluster.addNode(HOST, PORT2);

        cluster.addFile(HOST, PORT1, "/data/data10m64");
        cluster.addFile(HOST, PORT2, "/data/data10m64");
        // ...

        cluster.sort("/data/data10m64", "/data/data10m64Sorted");

    }

    @Test
    public void check() throws IOException {
        // check

        RandomAccessFile file = new RandomAccessFile("/Users/guanghuizeng/mmdpfs/user1".concat("/data/data100"), "r");

        int count = 0;
        List<Long> content = new ArrayList<>();

        for (int i = 0; i < file.length() / Long.BYTES; i++) {
            content.add(file.readLong());
            count++;
        }

        System.out.printf("total length = %d\n", file.length());

        System.out.printf("total count = %d\n", count);
        System.out.printf("size = %d\n", content.size());

        file.close();
    }
}
