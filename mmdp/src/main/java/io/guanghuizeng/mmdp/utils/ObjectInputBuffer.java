package io.guanghuizeng.mmdp.utils;


import java.io.*;
import java.nio.file.Path;

/**
 * Created by guanghuizeng on 16/3/14.
 *
 * primitive type is Long.
 */
public class ObjectInputBuffer implements Comparable<ObjectInputBuffer> {

    private ObjectInputStream buffer;
    private Long cache;

    public ObjectInputBuffer(File file) throws IOException {
        this.buffer = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
        reload();
    }

    public ObjectInputBuffer(Path path) throws IOException {
        this.buffer = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path.toFile())));
        reload();
    }

    public void close() throws IOException {
        this.buffer.close();
    }

    public boolean empty() {
        return this.cache == null;
    }

    public Long peek() {
        return this.cache;
    }

    public Long pop() throws IOException {
        Long answer = peek();// make a copy
        reload();
        return answer;
    }

    private void reload() throws IOException {
        if (buffer.available() > 0) {
            this.cache = this.buffer.readLong();
        } else {
            this.cache = null;
        }
    }

    public int compareTo(ObjectInputBuffer that) {
        return peek().compareTo(that.peek());
    }
}