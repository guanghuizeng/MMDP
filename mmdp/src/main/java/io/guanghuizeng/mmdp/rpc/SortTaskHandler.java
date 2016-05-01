package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.mmdp.SortSubTaskSpec;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 处理 Sort task
 */
public class SortTaskHandler extends SimpleChannelInboundHandler<SortSubTaskSpec> {

    BlockingQueue<SortSubTaskSpec> answers = new LinkedBlockingQueue<>();
    ChannelHandlerContext context;

    public void channelRead0(ChannelHandlerContext ctx, SortSubTaskSpec msg) {
        answers.add(msg);
    }

    public void channelRegistered(ChannelHandlerContext ctx) {
        context = ctx;
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public SortSubTaskSpec exec(SortSubTaskSpec spec) throws InterruptedException {
        ChannelFuture future = context.writeAndFlush(spec);
        future.sync();
        return answers.take();
    }
}
