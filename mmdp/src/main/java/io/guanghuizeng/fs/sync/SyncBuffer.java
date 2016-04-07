package io.guanghuizeng.fs.sync;

import io.netty.buffer.ByteBuf;

/**
 * Created by guanghuizeng on 16/4/7.
 */
public class SyncBuffer {

    /**
     * 基于 ByteBuf 设计缓存, 需要
     * - 处理读写操作
     * - 记录数据的来源
     * <p>
     * 那么, 方便 SyncService 做处理
     */

    public enum Function {
        READ, WRITE
    }

    private Function function; // 写入或是读取
    private String path;       // 文件路径
    private String mode;       // 文件的 mode, 参考 RAF
    private int position;      // 要处理的区块对应在文件中的起始位置
    private int length;        // 区块的长度

    private ByteBuf localBuffer;

    public SyncBuffer(int initCapacity, int maxCapacity) {

    }

    /**
     * IO
     */

    public SyncBuffer copy() {
        return null;
    }

    public void writeBytes(SyncBuffer buffer) {

    }

    public void clear() {
    }

    public int capacity() {
        return localBuffer.capacity();
    }

    public long readLong() {
        return localBuffer.readLong();
    }

    public int readableBytes() {
        return localBuffer.readableBytes();
    }


    /***
     *
     */

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
        return localBuffer;
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
        this.localBuffer = data;
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
}
