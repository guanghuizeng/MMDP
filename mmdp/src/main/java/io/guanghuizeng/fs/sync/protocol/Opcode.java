package io.guanghuizeng.fs.sync.protocol;

/**
 * Created by guanghuizeng on 16/4/12.
 */
public class Opcode {
    public static final byte WRITE = 0x00;
    public static final byte READ = 0x01;
    public static final byte APPEND = 0x10;
    public static final byte LENGTH = 0x11;
    public static final byte CLOSE = 0x12;


    public static void main(String[] args) {
        System.out.println(WRITE);
        System.out.println(READ);
        System.out.println(APPEND);
        System.out.println(LENGTH);
        System.out.println(CLOSE);
    }
}
