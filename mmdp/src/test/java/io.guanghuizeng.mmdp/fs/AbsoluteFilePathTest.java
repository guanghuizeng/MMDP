package io.guanghuizeng.mmdp.fs;

import io.guanghuizeng.fs.AbsoluteFilePath;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by guanghuizeng on 16/4/4.
 */
public class AbsoluteFilePathTest {
    @Test
    public void test0() {


        String prefix = "fs://";
        String host = "192.0.0.1";
        int port = 9372;
        String path = "/data";

        String fullPath = prefix.concat(host).concat(String.valueOf(port)).concat(path);
        AbsoluteFilePath absoluteFilePath = new AbsoluteFilePath(host, port, path);

        assertEquals(fullPath, absoluteFilePath.toString());

    }
}
