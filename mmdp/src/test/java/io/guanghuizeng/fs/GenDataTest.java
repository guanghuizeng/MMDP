package io.guanghuizeng.fs;

import io.guanghuizeng.mmdp.utils.GenData;
import org.junit.Test;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by guanghuizeng on 16/4/17.
 */
public class GenDataTest {

    @Test
    public void test0() throws IOException {
        String path = "/mmdpfs/gen/data30m64";
        GenData.gen(path, 1000 * 1000 * 30);
    }

    @Test
    public void test1() throws IOException {

        String path = "/mmdpfs/gen0/data30m64";
        GenData.gen0(path, 1000 * 1000 * 30);
    }
}
