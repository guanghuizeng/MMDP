package io.guanghuizeng.mmdp.algs2;

import com.google.common.primitives.Longs;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by guanghuizeng on 16/4/21.
 */
public class FileOutputBuffer {

    private BufferedOutputStream stream;

    public FileOutputBuffer(Path path) throws IOException {
        stream = new BufferedOutputStream(new FileOutputStream(path.toFile()));
    }

    public void writeLong(long n) throws IOException {
        byte[] bytes = Longs.toByteArray(n);
        stream.write(bytes);
        bytes = null;
    }

    public void close() throws IOException {
        stream.close();
    }
}