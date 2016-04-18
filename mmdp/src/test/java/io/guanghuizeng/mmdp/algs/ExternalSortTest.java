package io.guanghuizeng.mmdp.algs;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;

import static org.junit.Assert.assertTrue;


/**
 * Created by guanghuizeng on 16/3/13.
 */
public class ExternalSortTest {

    /****
     * External Sort 1
     */
    @Test
    public void test2() throws Exception {

        String source = System.getProperty("user.home").concat("/mmdpfs/gen0/data100m64");
        String target = System.getProperty("user.home").concat("/mmdpfs/sort0/data100m64");

        ExternalSort.sort(new File(source), new File(target));
    }

    /****
     * External Sort 3
     */
    @Test
    public void test3() throws Exception {

        String source = System.getProperty("user.home").concat("/mmdpfs/user1/data/data1k");
        String target = System.getProperty("user.home").concat("/mmdpfs/user1/data/data1kSorted");

        Path in = Paths.get(source);
        Path out = Paths.get(target);
        ExternalSort3.sort(in, out, Comparator.naturalOrder());

        // check
        // assertTrue(check2(out));

    }


    public static boolean check2(Path in) throws IOException {

        FileChannel inChannel = FileChannel.open(in, StandardOpenOption.READ);

        ByteBuf buf = Unpooled.buffer(1024);
        buf.writeBytes(inChannel, 0, 1024);

        long last = Long.MIN_VALUE;
        boolean result = true;

        while (buf.readableBytes() >= Long.BYTES) {
            long pre = buf.readLong();
            result = last <= pre;
            if (!result) {
                break;
            }
            last = pre;
        }
        inChannel.close();
        return result;
    }

}
