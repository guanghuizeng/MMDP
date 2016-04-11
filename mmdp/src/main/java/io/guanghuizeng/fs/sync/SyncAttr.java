package io.guanghuizeng.fs.sync;

import io.guanghuizeng.fs.AbsoluteFilePath;

/**
 * 用来生成 SyncMessage, 与 ByteBuf 对应
 */
public class SyncAttr {

    private AbsoluteFilePath path;
    private int first;
    private int last;

    public SyncAttr(AbsoluteFilePath path, int first, int last) {
        this.path = path;
        this.first = first;
        this.last = last;
    }

    public AbsoluteFilePath getPath() {
        return path;
    }

    public int getFirst() {
        return first;
    }

    public int getLast() {
        return last;
    }
}
