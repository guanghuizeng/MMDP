package io.guanghuizeng.mmdp.algs2;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import static org.junit.Assert.*;

/**
 *
 */
public class TopTest {

    @Test
    public void test0() throws IOException {

        File file = File.createTempFile("median", "test");
        file.deleteOnExit();
        FileOutputBuffer buffer = new FileOutputBuffer(file.toPath());
        ArrayList<Long> data = new ArrayList<>();
        Random random = new Random();
        int N = 100000;
        for (long i = 0; i < N; i++) {
            long n = Math.abs(random.nextLong() % 100);
            buffer.writeLong(n);
            data.add(n);
        }
        buffer.close();

        Map<Long,Long> expected = new TreeMap<>();
        for (Long n : data) {
            if (expected.containsKey(n)) {
                expected.put(n, expected.get(n) + 1);
            } else {
                expected.put(n, 1L);
            }
        }

        List<Map.Entry<Long,Long>> list = new LinkedList<>(expected.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Long, Long>>() {
            @Override
            public int compare(Map.Entry<Long, Long> o1, Map.Entry<Long, Long> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        Map<Long, Long> result = Top.search(file.toPath(), 100, -100, 10);
        List<Map.Entry<Long,Long>> list2 = new LinkedList<>(result.entrySet());
        Collections.sort(list2, new Comparator<Map.Entry<Long, Long>>() {
            @Override
            public int compare(Map.Entry<Long, Long> o1, Map.Entry<Long, Long> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        assertEquals(list.subList(0,list2.size()), list2);
    }

    @Test
    public void test1() throws IOException {
        /**
         * 测试性能
         */

        File file = File.createTempFile("median", "test");
        file.deleteOnExit();
        FileOutputBuffer buffer = new FileOutputBuffer(file.toPath());

        Random random = new Random();
        int N = 10000;
        for (long i = 0; i < N; i++) {
            long n = Math.abs(random.nextLong());
            buffer.writeLong(n);
        }
        buffer.close();

        Map<Long, Long> result = Top.search(file.toPath(), 100, -100, 10);
        List<Map.Entry<Long,Long>> list2 = new LinkedList<>(result.entrySet());
        Collections.sort(list2, new Comparator<Map.Entry<Long, Long>>() {
            @Override
            public int compare(Map.Entry<Long, Long> o1, Map.Entry<Long, Long> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        for (Map.Entry<Long, Long> e : list2) {
            System.out.println("Key: " + e.getKey() + " value: " + e.getValue());
        }
    }


    @Test
    public void test2() throws IOException {
        /**
         * 测试性能
         */

        File file = File.createTempFile("median", "test");
        file.deleteOnExit();
        FileOutputBuffer buffer = new FileOutputBuffer(file.toPath());

        Random random = new Random();
        int N = 10000000;
        for (long i = 0; i < N; i++) {
            long n = Math.abs(random.nextLong());
            buffer.writeLong(n);
        }
        buffer.close();

        Map<Long, Long> result = Top2.search(file.toPath(), Long.MAX_VALUE, Long.MIN_VALUE, 10);
        List<Map.Entry<Long,Long>> list2 = new LinkedList<>(result.entrySet());
        Collections.sort(list2, new Comparator<Map.Entry<Long, Long>>() {
            @Override
            public int compare(Map.Entry<Long, Long> o1, Map.Entry<Long, Long> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        for (Map.Entry<Long, Long> e : list2) {
            System.out.println("Key: " + e.getKey() + " value: " + e.getValue());
        }
    }

    @Test
    public void test3() throws IOException {
        /**
         * 测试正确性
         */

        File file = File.createTempFile("median", "test");
        file.deleteOnExit();
        FileOutputBuffer buffer = new FileOutputBuffer(file.toPath());
        ArrayList<Long> data = new ArrayList<>();
        Random random = new Random();
        int N = 100000;
        for (long i = 0; i < N; i++) {
            long n = Math.abs(random.nextLong() % 10000);
            buffer.writeLong(n);
            data.add(n);
        }
        buffer.close();

        Map<Long,Long> expected = new TreeMap<>();
        for (Long n : data) {
            if (expected.containsKey(n)) {
                expected.put(n, expected.get(n) + 1);
            } else {
                expected.put(n, 1L);
            }
        }

        List<Map.Entry<Long,Long>> list = new LinkedList<>(expected.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Long, Long>>() {
            @Override
            public int compare(Map.Entry<Long, Long> o1, Map.Entry<Long, Long> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        Map<Long, Long> result = Top2.search(file.toPath(), 10000, -10000, 10);
        List<Map.Entry<Long,Long>> list2 = new LinkedList<>(result.entrySet());
        Collections.sort(list2, new Comparator<Map.Entry<Long, Long>>() {
            @Override
            public int compare(Map.Entry<Long, Long> o1, Map.Entry<Long, Long> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        assertEquals(list.subList(0,list2.size()), list2);
    }
}
