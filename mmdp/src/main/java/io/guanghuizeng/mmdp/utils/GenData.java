package io.guanghuizeng.mmdp.utils;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GenData {

    public static void write(File output, long count) throws IOException {
        FileOutputBuffer buffer = new FileOutputBuffer(output);
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            buffer.writeLong(random.nextLong());
        }
        buffer.close();
    }

    public static void statics(File data) {
        try {
            FileInputBuffer buffer = new FileInputBuffer(data);
            int count = 0;
            int total = 0;
            while (!buffer.empty()) {
                long value = buffer.pop();
                total++;
                if (value > 0) {
                    count++;
                }
            }
            System.out.printf("%s \n - 包括 %d 个64 bit整数\n", data.getCanonicalPath(), total);
            System.out.printf(" - 正数比率为: %f\n", (float) count / total);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void gen(String path, long count) throws IOException {
        File output = new File(path);
        write(output, count);
    }

    /**
     * @param args, args[0] -> address of output file, args[1] -> count
     */
    public static void main(String[] args) {

        File output = new File(args[0]);
        long count = Long.parseLong(args[1]);

        try {
            write(output, count);
            statics(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
