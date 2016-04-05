package io.guanghuizeng.mmdp.utils;

import java.io.*;

/**
 * Created by guanghuizeng on 16/3/14.
 * <p>
 * 对字节流做一个简单的封装
 */
public class FileOutputBuffer {

    private ObjectOutputStream buffer;

    public FileOutputBuffer(File file) throws IOException {
        this.buffer = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
    }

    public void writeLong(Long number) throws IOException {
        buffer.writeLong(number);
    }

    public void writeInt(int number) throws IOException {
        buffer.writeInt(number);
    }

    public void close() throws IOException {
        buffer.close();
    }

}
