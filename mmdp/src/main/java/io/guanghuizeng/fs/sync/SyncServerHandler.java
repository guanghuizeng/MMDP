package io.guanghuizeng.fs.sync;

import io.guanghuizeng.fs.sync.protocol.SyncMessage;
import io.guanghuizeng.fs.sync.protocol.Opcode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by guanghuizeng on 16/4/13.
 */
public class SyncServerHandler extends SimpleChannelInboundHandler<SyncMessage> {

    private String home;

    public SyncServerHandler(String home) {
        this.home = home;
    }

    public SyncMessage read(SyncMessage msg) throws IOException {

        RandomAccessFile file = new RandomAccessFile(pathResolve(msg.path()), "r");
        ByteBuf buf = Unpooled.directBuffer((int) msg.length());
        FileChannel channel = file.getChannel();
        buf.writeBytes(channel, (int) msg.position(), (int) msg.length()); // TODO 类型不一致.
        file.close();
        channel.close();
        return new SyncMessage(msg.opCode(), msg.path(), msg.position(), msg.length(), buf);
    }

    public SyncMessage write(SyncMessage msg) throws IOException {
        RandomAccessFile file = new RandomAccessFile(pathResolve(msg.path()), "rw");
        FileChannel channel = file.getChannel();
        ByteBuffer buffer = msg.content().nioBuffer();
        channel.position(channel.size());
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
        channel.close();
        file.close();
        msg.clearContent();
        return msg;
    }

    public SyncMessage append(SyncMessage msg) throws IOException {
        RandomAccessFile file = new RandomAccessFile(pathResolve(msg.path()), "rw");
        FileChannel channel = file.getChannel();
        ByteBuffer buffer = msg.content().nioBuffer();
        channel.position(channel.size());
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
        channel.close();
        file.close();
        msg.clearContent();
        return msg;
    }

    public SyncMessage length(SyncMessage msg) throws IOException {
        File file = new File(pathResolve(msg.path()));
        ByteBuf buf = Unpooled.directBuffer();
        buf.writeLong(file.length());
        return new SyncMessage(
                msg.opCode(), msg.path(), msg.position(), buf);
    }

    /**
     * 选择合适的处理函数
     *
     * @param msg
     * @return
     */
    public SyncMessage execute(SyncMessage msg) throws IOException {
        switch (msg.opCode()) {
            case Opcode.READ:
                return read(msg);
            case Opcode.WRITE:
                return write(msg);
            case Opcode.APPEND:
                return append(msg);
            case Opcode.LENGTH:
                return length(msg);
            default:
                return msg;
        }
    }


    public void channelRead0(ChannelHandlerContext ctx, SyncMessage msg)
            throws IOException {
        if (msg.opCode() == Opcode.CLOSE) {
            ctx.writeAndFlush(msg);
            ctx.close();
        }
        ctx.writeAndFlush(execute(msg));
    }

    private String pathResolve(String path) {
        return System.getProperty("user.home").concat("/mmdpfs").concat(home).concat(path);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
