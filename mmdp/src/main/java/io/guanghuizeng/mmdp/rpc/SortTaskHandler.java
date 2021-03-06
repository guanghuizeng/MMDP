package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.mmdp.SortSubTaskSpec;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 处理 Sort task
 */
public class SortTaskHandler extends ChannelInboundHandlerAdapter {

    BlockingQueue<SortSubTaskSpec> answers = new LinkedBlockingQueue<>();
    ChannelHandlerContext context;

    public boolean acceptInboundMessage(Object msg) {
        return msg instanceof SortSubTaskSpec;
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (acceptInboundMessage(msg)) {
            answers.add((SortSubTaskSpec) msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        context = ctx;
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }

    public SortSubTaskSpec exec(SortSubTaskSpec spec) throws InterruptedException {
        assert context != null;
        ChannelFuture future = context.writeAndFlush(spec);
        future.sync();
        return answers.take();
    }
}
