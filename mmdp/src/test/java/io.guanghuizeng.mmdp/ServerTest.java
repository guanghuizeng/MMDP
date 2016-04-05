package io.guanghuizeng.mmdp;

/**
 * Created by guanghuizeng on 16/4/3.
 */
public class ServerTest {

    public static void main(String[] args) throws Exception {
        Server server = new Server("user1/");
        server.start();
    }
}