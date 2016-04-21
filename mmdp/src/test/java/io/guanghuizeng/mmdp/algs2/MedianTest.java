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


    public void test3(long start, int N) throws IOException {

        // unsigned

        File file = File.createTempFile("median", "test");
        file.deleteOnExit();
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));

        ArrayList<Long> data = new ArrayList<>();

        for (long i = start; i < start + N; i++) {
            stream.writeLong(i);
            data.add(i);
        }
        stream.close();

        data.sort(Comparator.naturalOrder());
        Long expected = data.get((N - 1) / 2);

        Engine engine = new Engine();
        long result = engine.median(Paths.get(file.getPath()), N);

        System.out.printf("Exp: %d, Rst: %d\n", expected, result);
        assertEquals(Long.toUnsignedString(expected), Long.toUnsignedString(result));
    }

    @Test
    public void test31() throws IOException {
        // TODO 修复 bug
        int N = 100000;
        long start = 4;

        for (int i = 0; i < 14; i++) {
            System.out.printf("i: %d, ", i);
            test3(start, N);
            start = start * 10;
        }
    }

    @Test
    public void debug() throws IOException {

        /**
         * fail: 超过
         */


        File file = File.createTempFile("median", "test");
        file.deleteOnExit();
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));

        ArrayList<Long> data = new ArrayList<>();

        long start = 4 * (long) Math.pow(10, 10);
        int N = 2;

        for (long i = start; i < start + N; i++) {
            stream.writeLong(i);
            data.add(i);
        }
        stream.close();

        data.sort(Comparator.naturalOrder());
        Long expected = data.get((N - 1) / 2);

        Engine engine = new Engine();
        long result = engine.median(Paths.get(file.getPath()), N);

        System.out.printf("Exp: %d, Rst: %d\n", expected, result);
    }

    @Test
    public void debug0() throws IOException {

        /**
         * pass
         */

        File file = File.createTempFile("median", "test");
        file.deleteOnExit();
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));

        ArrayList<Long> data = new ArrayList<>();

        long start = 4 * (long) Math.pow(10, 9);
        int N = 10000;

        for (long i = start; i < start + N; i++) {
            stream.writeLong(i);
            data.add(i);
        }
        stream.close();

        data.sort(Comparator.naturalOrder());
        Long expected = data.get((N - 1) / 2);

        Engine engine = new Engine();
        long result = engine.median(Paths.get(file.getPath()), N);

        System.out.printf("Exp: %d, Rst: %d\n", expected, result);
    }
}
