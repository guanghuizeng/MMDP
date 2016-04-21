package io.guanghuizeng.mmdp.algs2;

import io.guanghuizeng.mmdp.algs.MedianFinder64;
import io.guanghuizeng.mmdp.engine.Engine;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
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

        long expected = (start + N - 1) / 2;

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
    public void test1() throws IOException {

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

        long expected = (start + N - 1) / 2;

        Engine engine = new Engine();
        long result = engine.median(Paths.get(file.getPath()), N);
        assertEquals(expected, result);

    }

    @Test
    public void test11() throws IOException {
        for (int i = 0; i < 50; i++) {
            test1();
        }
    }

    @Test
    public void test2() throws IOException {

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

        MedianFinder64 finder = new MedianFinder64();
        long result1 = finder.find(file, N);

        Engine engine = new Engine();
        long result2 = engine.median(Paths.get(file.getPath()), N);

        assertEquals(result1, result2);

    }

    @Test
    public void test21() throws IOException {
        for (int i = 0; i < 50; i++) {
            test2();
        }
    }
}
