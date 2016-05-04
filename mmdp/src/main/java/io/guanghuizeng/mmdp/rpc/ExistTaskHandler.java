package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.mmdp.ExistSubTaskSpec;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 处理 Exist Task
 */
public class ExistTaskHandler extends ChannelInboundHandlerAdapter {

    private BlockingQueue<ExistSubTaskSpec> answers = new LinkedBlockingQueue<>();
    private ChannelHandlerContext context;

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (acceptInboundMessage(msg)) {
            answers.add((ExistSubTaskSpec) msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    public boolean acceptInboundMessage(Object msg) {
        return msg instanceof ExistSubTaskSpec;
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

    public ExistSubTaskSpec exec(ExistSubTaskSpec spec) throws InterruptedException {
        assert context != null;
        ChannelFuture future = context.writeAndFlush(spec);
        future.sync();
        return answers.take();
    }
}
