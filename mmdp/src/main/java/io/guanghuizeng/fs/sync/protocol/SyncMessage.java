package io.guanghuizeng.fs.sync.protocol;

import io.guanghuizeng.fs.sync.SyncBuffer;
import io.netty.buffer.ByteBuf;

/**
 * Created by guanghuizeng on 16/4/7.
 */
public class SyncMessage {

    public enum Function {READ, WRITE}

    /**
     * SyncClient 和 SyncServer 之间的沟通协议
     */
    private Function function; // 写入或是读取
    private String path;       // 文件路径
    private String mode;       // 文件的 mode, 参考 RAF
    private int position;      // 要处理的区块对应在文件中的起始位置
    private int length;        // 区块的长度
    private ByteBuf data;      // 存储数据区块

    public SyncMessage(Function function, String path, String mode, int position, int length, ByteBuf data) {
        this.function = function;
        this.path = path;
        this.mode = mode;
        this.position = position;
        this.length = length;
        this.data = data;
    }

    public String getPath() {
        return path;
    }

    public String getMode() {
        return mode;
    }

    public int getPosition() {
        return position;
    }

    public ByteBuf getData() {
        return data;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setData(ByteBuf data) {
        this.data = data;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    /*************
     * static methods
     *************/

    public static SyncMessage build(SyncBuffer buffer) {
        return null;
    }
}


