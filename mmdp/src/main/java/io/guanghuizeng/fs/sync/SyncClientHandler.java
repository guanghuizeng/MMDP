package io.guanghuizeng.fs.sync;

import io.guanghuizeng.fs.sync.protocol.Opcode;
import io.guanghuizeng.fs.sync.protocol.SyncMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by guanghuizeng on 16/4/13.
 */
public class SyncClientHandler extends SimpleChannelInboundHandler<SyncMessage> {

    private ChannelHandlerContext context;
    private BlockingQueue<SyncMessage> answers = new LinkedBlockingQueue<>();

    public void push(ByteBuf buf, SyncAttr attr) throws InterruptedException {
        SyncMessage message = new SyncMessage(Opcode.APPEND, attr.getPath().getActualPath().toString(),
                attr.getPosition(), buf);
        ChannelFuture future = context.channel().writeAndFlush(message);
        future.sync();
        SyncMessage answer = answers.take();
    }

    public ByteBuf pool(SyncAttr attr) throws InterruptedException {

        SyncMessage message = new SyncMessage(Opcode.READ, attr.getPath().getActualPath().toString(),
                attr.getPosition(), attr.getLength());
        ChannelFuture future = context.channel().writeAndFlush(message);
        future.sync();

        // take answer
        SyncMessage answer = answers.take();


        return answer.content();
    }

    public long length(SyncAttr attr) throws InterruptedException {
        SyncMessage message = new SyncMessage(Opcode.LENGTH, attr.getPath().getActualPath().toString(),
                attr.getPosition(), Unpooled.EMPTY_BUFFER);
        ChannelFuture future = context.channel().writeAndFlush(message);
        future.sync();

        // take answer
        SyncMessage answer = answers.take();
        return answer.content().readLong();
    }

    public boolean close() throws InterruptedException {
        SyncMessage message = new SyncMessage(Opcode.CLOSE, "", 0, Unpooled.EMPTY_BUFFER);
        ChannelFuture future = context.channel().writeAndFlush(message);
        future.sync();
        SyncMessage answer = answers.take();
        assert answer.opCode() == Opcode.CLOSE;
        return true;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    public void channelRead0(ChannelHandlerContext ctx, SyncMessage msg) throws Exception {
        // super.channelRead(ctx, msg);
        // TODO 增加逻辑
        answers.add(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
