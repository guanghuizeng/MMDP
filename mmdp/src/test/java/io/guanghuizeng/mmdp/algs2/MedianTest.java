package io.guanghuizeng.mmdp.algs2;

import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.mmdp.algs.MedianFinder64;
import io.guanghuizeng.mmdp.Engine;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;


public class MedianTest {


    @Test
    public void test0() throws IOException {

        File file = File.createTempFile("median", "test");
        file.deleteOnExit();
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));

        Random random = new Random();

        long start = random.nextLong();
        long N = 10 * 1000;

        for (long i = start; i < start + N; i++) {
            stream.writeLong(i);
        }
        stream.close();

        long expected = (start * 2 + N - 1) / 2;

        MedianFinder64 finder = new MedianFinder64();
        long result = finder.find(file, N);

        assertEquals(expected, result);
    }

    @Test
    public void test01() throws IOException {
        for (int i = 0; i < 50; i++) {
            test0();
        }
    }

    @Test
    public void test3() throws IOException {

        // unsigned

        File file = File.createTempFile("median", "test");
        file.deleteOnExit();
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));

        ArrayList<Long> data = new ArrayList<>();

        Random random = new Random();
        int N = 10000;
        for (long i = 0; i < N; i++) {
            long n = Math.abs(random.nextLong());
            stream.writeLong(n);
            data.add(n);
        }
        stream.close();

        data.sort(Comparator.naturalOrder());
        Long expected = data.get((N - 1) / 2);

        Engine engine = new Engine(new FileSystem());
        long result = engine.median(Paths.get(file.getPath()), N);

        assertEquals(Long.toUnsignedString(expected), Long.toUnsignedString(result));
    }

    @Test
    public void test31() throws IOException {
        for (int i = 0; i < 20; i++) {
            test3();
        }
    }
}
