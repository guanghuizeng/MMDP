package io.guanghuizeng.fs.sync;

import io.guanghuizeng.fs.sync.protocol.Opcode;
import io.guanghuizeng.fs.sync.protocol.SyncMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by guanghuizeng on 16/4/13.
 */
public class SyncClientHandler extends SimpleChannelInboundHandler<SyncMessage> {

    private ChannelHandlerContext context;

    public void push(ByteBuf buf, SyncAttr attr) throws InterruptedException {
        SyncMessage message = new SyncMessage(Opcode.APPEND, attr.getPath().getActualPath(),
                attr.getFirst(), buf);
        ChannelFuture future = context.channel().writeAndFlush(message);
        future.sync();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    public void channelRead0(ChannelHandlerContext ctx, SyncMessage msg) throws Exception {
        // super.channelRead(ctx, msg);
        // TODO 增加逻辑
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
