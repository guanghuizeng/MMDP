package io.guanghuizeng.fs.sync.protocol;

import io.netty.buffer.*;

/**
 * Created by guanghuizeng on 16/4/12.
 */
public class SyncMessage {

    private int magic = 0x90;
    private byte opCode;
    private String path;
    private long position;
    private ByteBuf content;

    public SyncMessage(byte opCode, String path, long position, ByteBuf content) {
        this.opCode = opCode;
        this.path = path;
        this.position = position;
        this.content = content.copy();
    }


    public int magic() {
        return magic;
    }

    public byte opCode() {
        return opCode;
    }

    public String path() {
        return path;
    }

    public long position() {
        return position;
    }

    public ByteBuf content() {
        return content;
    }

    public void clearContent() {
        content = Unpooled.buffer(0);
    }
}
