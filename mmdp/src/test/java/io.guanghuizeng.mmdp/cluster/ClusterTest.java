package io.guanghuizeng.mmdp.cluster;

import io.guanghuizeng.mmdp.Cluster;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by guanghuizeng on 16/4/4.
 */
public class ClusterTest {

    @Test
    public void testEcho() throws Exception {

        String HOST = System.getProperty("host", "127.0.0.1");
        int PORT = Integer.parseInt(System.getProperty("port", "8090"));

        Cluster cluster = new Cluster();

        cluster.addNode(HOST, PORT);
        cluster.addNode(HOST, PORT);

        List<String> response = new ArrayList<>();
        response.add("hi");
        response.add("hi");

        assertEquals(response, cluster.echo());
    }

    @Test
    public void testEcho2() throws Exception {

        // 连接两个服务器端口

        String HOST = System.getProperty("host", "127.0.0.1");
        int PORT1 = Integer.parseInt(System.getProperty("port", "8090"));
        int PORT2 = Integer.parseInt(System.getProperty("port", "8091"));

        Cluster cluster = new Cluster();

        cluster.addNode(HOST, PORT1);
        cluster.addNode(HOST, PORT2);

        List<String> response = new ArrayList<>();
        response.add("hi");
        response.add("hi");

        assertEquals(response, cluster.echo());
    }

    @Test
    public void testSort() throws Exception {
        String HOST = System.getProperty("host", "127.0.0.1");
        int PORT1 = Integer.parseInt(System.getProperty("port", "8090"));
        int PORT2 = Integer.parseInt(System.getProperty("port", "8091"));

        Cluster cluster = new Cluster();

        cluster.addNode(HOST, PORT1);
        cluster.addNode(HOST, PORT2);

        cluster.addFile(HOST, PORT1, "/data/data1k");
        cluster.addFile(HOST, PORT2, "/data/data1k");
        // ...

        cluster.sort("/data/data1k", "");
    }
}
