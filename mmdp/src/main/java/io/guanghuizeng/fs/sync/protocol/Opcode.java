package io.guanghuizeng.fs.sync.protocol;

/**
 * Created by guanghuizeng on 16/4/12.
 */
public class Opcode {
    public static final byte WRITE = 0x00;
    public static final byte READ = 0x01;
    public static final byte APPEND = 0x10;
    public static final byte LENGTH = 0x11;
}
