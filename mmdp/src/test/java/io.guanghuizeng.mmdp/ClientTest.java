package io.guanghuizeng.mmdp;

import org.junit.Test;

/**
 * Created by guanghuizeng on 16/4/2.
 */
public class ClientTest {

    @Test
    public void testSort1() throws Exception {

        Client client = (new Client()).start();

        String source = "data/data1k";
        String target = "data/data1kSorted";

        client.sort(source, target);

    }
}
