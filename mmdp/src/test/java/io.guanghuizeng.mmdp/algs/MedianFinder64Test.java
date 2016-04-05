package io.guanghuizeng.mmdp.algs;

import io.guanghuizeng.mmdp.utils.FileOutputBuffer;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Created by guanghuizeng on 16/3/17.
 */
public class MedianFinder64Test {

    @Test
    public void test1() throws IOException {

        long N = 1000L;
        File data = File.createTempFile("data", String.valueOf(N));
        List<Long> dataList = new ArrayList<>();

        /* data generating */
        FileOutputBuffer buffer = new FileOutputBuffer(data);
        Random random = new Random();
        for (long i = 0; i < N; i++) {
            dataList.add(i);
            buffer.writeLong(i);
        }
        buffer.close();


        /* */
        MedianFinder64 finder = new MedianFinder64();
        long result = finder.find(data, N);

        dataList.sort(Comparator.<Long>naturalOrder());
        long mid = dataList.get((int) (N - 1) / 2);

        assertEquals(mid, result);
    }

    /** TODO: 负数问题没有解决
     *
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {

        long N = 1000L;
        File data = File.createTempFile("data", String.valueOf(N));
        List<Long> dataList = new ArrayList<>();

        /* data generating */
        FileOutputBuffer buffer = new FileOutputBuffer(data);
        Random random = new Random();
        for (long i = 0; i < N; i++) {
            long n = random.nextLong();
            dataList.add(n);
            buffer.writeLong(n);
        }
        buffer.close();


        /* */
        MedianFinder64 finder = new MedianFinder64();
        long result = finder.find(data, N);

        dataList.sort(Comparator.<Long>naturalOrder());
        long mid = dataList.get((int) (N - 1) / 2);

        assertEquals(mid, result);
    }
}
