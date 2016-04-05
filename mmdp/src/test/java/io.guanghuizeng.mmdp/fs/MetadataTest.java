package io.guanghuizeng.mmdp.fs;

import io.guanghuizeng.fs.Metadata;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by guanghuizeng on 16/4/4.
 */
public class MetadataTest {

    @Test
    public void test0() {
        Metadata metadata = new Metadata();

        String prefix = "fs://";
        String host = "192.0.0.1";
        int port = 8923;
        String path = "/data/test0";

        String fullPath = prefix.concat(host).concat(String.valueOf(port)).concat(path);


        prefix = "fs://";
        host = "192.0.0.1";
        port = 8990;
        path = "/data/test0";

    }
}
