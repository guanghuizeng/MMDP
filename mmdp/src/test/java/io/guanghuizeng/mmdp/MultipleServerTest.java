package io.guanghuizeng.mmdp;

import org.junit.Test;

/**
 * Created by guanghuizeng on 16/4/3.
 */
public class MultipleServerTest {

    @Test
    public void server2() throws Exception {
        Server server = new Server("user2/");
        server.start(8091);
    }
}