package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.mmdp.MaxSubTaskSpec;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 处理 Max Sub Task
 */
public class MaxTaskHandler extends ChannelInboundHandlerAdapter {

    private BlockingQueue<MaxSubTaskSpec> answers = new LinkedBlockingQueue<>();
    private ChannelHandlerContext context;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (acceptInboundMessage(msg)) {
            answers.add((MaxSubTaskSpec) msg);
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

    public boolean acceptInboundMessage(Object msg) {
        return msg instanceof MaxSubTaskSpec;
    }

    public MaxSubTaskSpec exec(MaxSubTaskSpec spec) throws InterruptedException {
        assert context != null;
        ChannelFuture future = context.writeAndFlush(spec);
        future.sync();
        return answers.take();
    }
}
