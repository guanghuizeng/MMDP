package io.guanghuizeng.mmdp.utils;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by guanghuizeng on 16/4/3.
 */
public class GenDataTest {

    @Test
    public void gen() throws IOException {

        String root = "/Users/guanghuizeng/code/projects/MMDP/example/mmdpfs/";
        String target = "data/data1k";
        long N = 1000L;

        GenData.gen(root.concat(target), N);
    }
}
