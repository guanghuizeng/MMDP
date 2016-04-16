package io.guanghuizeng.fs.sync;

import io.guanghuizeng.fs.sync.protocol.SyncMessage;
import io.guanghuizeng.fs.sync.protocol.Opcode;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by guanghuizeng on 16/4/13.
 */
public class SyncServerHandler extends SimpleChannelInboundHandler<SyncMessage> {


    public SyncMessage read(SyncMessage msg) throws FileNotFoundException {

        RandomAccessFile file = new RandomAccessFile(msg.path(), "r");

        return msg;
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
            default:
                return msg;
        }
    }


    public void channelRead0(ChannelHandlerContext ctx, SyncMessage msg)
            throws IOException {
        ctx.writeAndFlush(execute(msg));
    }

    private String pathResolve(String path) {
        return System.getProperty("user.home").concat("/mmdpfs").concat(path);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
