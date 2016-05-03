package io.guanghuizeng.fs.input;

/**
 * Created by guanghuizeng on 16/4/16.
 */
public class VirtualFileInputBuffer implements Comparable<VirtualFileInputBuffer> {

    private VirtualFileInput input;
    private Long buffer = null;

    public VirtualFileInputBuffer(VirtualFileInput input) throws InterruptedException {
        this.input = input;
        reload();
    }

    public long pop() throws InterruptedException {
        Long answer = peek();
        reload();
        return answer;
    }

    public Long peek() {
        return buffer;
    }

    public boolean isEmpty() {
        return buffer == null;
    }

    public void close() throws InterruptedException {
        input.close();
    }

    public void reload() throws InterruptedException {
        try {
            buffer = input.readLong();
        } catch (IndexOutOfBoundsException e) {
            buffer = null;
            // throw e;
        }
    }

    public int compareTo(VirtualFileInputBuffer that) {
        return peek().compareTo(that.peek());
    }
}
