package io.guanghuizeng.mmdp;

/**
 * Created by guanghuizeng on 16/4/3.
 */
public class MultipleServerTest {

    public static void main(String[] args) throws Exception {
        Server server0 = new Server("user2/");
        server0.start(8091);
    }
}