package io.guanghuizeng.mmdp.algs2;

import com.google.common.primitives.Longs;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by guanghuizeng on 16/4/21.
 */
public class FileInputBuffer implements Comparable<FileInputBuffer> {

    private BufferedInputStream stream;
    private boolean isEmpty = true;
    private long buffer;

    public FileInputBuffer(Path path) throws IOException {
        stream = new BufferedInputStream(new FileInputStream(path.toFile()));
        reload();
    }

    public boolean empty() {
        return isEmpty;
    }

    public void close() throws IOException {
        stream.close();
    }

    public Long peek() {
        return buffer;
    }

    public Long pop() throws IOException {
        Long value = buffer;
        reload();
        return value;
    }

    private void reload() throws IOException {
        if (stream.available() >= Long.BYTES) {
            byte[] in = new byte[Long.BYTES];
            stream.read(in);
            buffer = Longs.fromByteArray(in);
            in = null;
            isEmpty = false;
        } else {
            isEmpty = true;
        }
    }

    // TODO 增加控制选项
    public int compareTo(FileInputBuffer that) {
        return peek().compareTo(that.peek());
    }
}
