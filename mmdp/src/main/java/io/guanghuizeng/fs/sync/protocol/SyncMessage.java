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
    private long length;
    private ByteBuf content= Unpooled.EMPTY_BUFFER;

    public SyncMessage(byte opCode, String path, long position, ByteBuf content) {
        this.opCode = opCode;
        this.path = path;
        this.position = position;
        this.content = content.copy();
    }

    public SyncMessage(byte opCode, String path, long position, long length) {
        this.opCode = opCode;
        this.path = path;
        this.position = position;
        this.length = length;
    }

    public SyncMessage(byte opCode, String path, long position, long length, ByteBuf content) {
        this.opCode = opCode;
        this.path = path;
        this.position = position;
        this.length = length;
        this.content = content;
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

    public long length() {
        return length;
    }

    public ByteBuf content() {
        return content;
    }

    public void clearContent() {
        content = Unpooled.buffer(0);
    }
}
