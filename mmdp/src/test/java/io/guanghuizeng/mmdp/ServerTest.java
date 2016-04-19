package io.guanghuizeng.mmdp;

import org.junit.Test;

/**
 * Created by guanghuizeng on 16/4/3.
 */
public class ServerTest {

    @Test
    public void server1() throws Exception {
        Server server = new Server("user1/");
        server.start();
    }
}