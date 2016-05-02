package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.mmdp.MedianSubTaskSpec;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 处理 Median Sub Task
 */
public class MedianTaskHandler extends ChannelInboundHandlerAdapter {

    private BlockingQueue<MedianSubTaskSpec> answers = new LinkedBlockingQueue<>();
    private ChannelHandlerContext context;

    public boolean acceptInboundMessage(Object msg) {
        return msg instanceof MedianSubTaskSpec;
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (acceptInboundMessage(msg)) {
            answers.add((MedianSubTaskSpec) msg);
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

    public MedianSubTaskSpec exec(MedianSubTaskSpec spec) throws InterruptedException {
        assert context != null;
        ChannelFuture future = context.writeAndFlush(spec);
        future.sync();
        return answers.take();
    }
}