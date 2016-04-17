package io.guanghuizeng.fs.sync;

import io.guanghuizeng.fs.AbsoluteFilePath;

/**
 * 用来生成 SyncMessage, 与 ByteBuf 对应
 */
public class SyncAttr {

    private AbsoluteFilePath path;
    private long position;
    private long length;

    public SyncAttr(AbsoluteFilePath path) {
        this.path = path;
    }

    public SyncAttr(AbsoluteFilePath path, long position, long length) {
        this.path = path;
        this.position = position;
        this.length = length;
    }

    public AbsoluteFilePath getPath() {
        return path;
    }

    public long getPosition() {
        return position;
    }

    public long getLength() {
        return length;
    }
}
