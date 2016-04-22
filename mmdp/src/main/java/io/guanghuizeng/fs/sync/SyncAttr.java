package io.guanghuizeng.fs.sync;

import io.guanghuizeng.fs.Uri;

/**
 * 用来生成 SyncMessage, 与 ByteBuf 对应
 */
public class SyncAttr {

    private Uri path;
    private long position;
    private long length;

    public SyncAttr(Uri path) {
        this.path = path;
    }

    public SyncAttr(Uri path, long position, long length) {
        this.path = path;
        this.position = position;
        this.length = length;
    }

    public Uri getPath() {
        return path;
    }

    public long getPosition() {
        return position;
    }

    public long getLength() {
        return length;
    }
}
