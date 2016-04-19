package io.guanghuizeng.mmdp.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

/**
 * 应用 Netty 和 FileChannel, 生成数据, 写入到文件中
 */
public class GenData {

    public static void gen(String relativePath, long count) throws IOException {

        Path path = Paths.get(System.getProperty("user.home").concat(relativePath));
        FileChannel channel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        int quantity = 1024 * 1024;
        int bufSize = quantity * Long.BYTES;

        ByteBuf buf = Unpooled.directBuffer(bufSize);

        long round = count / quantity;
        long remain = count % quantity;

        Random random = new Random();
        for (int i = 0; i < round; i++) {
            for (int j = 0; j < quantity; j++) {
                buf.writeLong(random.nextLong());
            }
            buf.readBytes(channel, channel.position(), bufSize);
            channel.position(channel.position() + bufSize);
            buf.clear();
        }

        buf.clear();
        for (int i = 0; i < remain; i++) {
            buf.writeLong(random.nextLong());
        }
        int remainBytes = buf.readableBytes();
        buf.readBytes(channel, channel.position(), remainBytes);
        channel.position(channel.position() + remainBytes);

        buf.release();
        channel.close();
    }

    public static void gen0(String relativePath, long count) throws IOException{

        File file = new File(System.getProperty("user.home").concat(relativePath));
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        Random random = new Random();
        for (long   i = 0; i < count; i++) {
            oos.writeLong(random.nextLong());
        }

        oos.close();
    }
}
