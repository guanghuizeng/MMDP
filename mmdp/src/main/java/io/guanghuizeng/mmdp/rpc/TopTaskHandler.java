package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.mmdp.TopSubTaskSpec;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by guanghuizeng on 16/5/6.
 */
public class TopTaskHandler extends ChannelInboundHandlerAdapter {

    private BlockingQueue<TopSubTaskSpec> answers = new LinkedBlockingQueue<>();
    private ChannelHandlerContext context;

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (acceptInboundMessage(msg)) {
            answers.add((TopSubTaskSpec) msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    public boolean acceptInboundMessage(Object msg) {
        return msg instanceof TopSubTaskSpec;
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

    public TopSubTaskSpec exec(TopSubTaskSpec spec) throws InterruptedException {
        assert context != null;
        ChannelFuture future = context.writeAndFlush(spec);
        future.sync();
        return answers.take();
    }
}
