package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.mmdp.EngineBackendExecutor;
import io.guanghuizeng.mmdp.MedianSubTaskSpec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;

/**
 *
 */
public class MedianTaskServerHandler extends SimpleChannelInboundHandler<MedianSubTaskSpec> {

    private EngineBackendExecutor executor;

    public MedianTaskServerHandler(EngineBackendExecutor executor) {
        this.executor = executor;
    }

    public void channelRead0(ChannelHandlerContext context, MedianSubTaskSpec msg) {
        try {
            context.writeAndFlush(executor.exec(msg));
        } catch (IOException e) {
            exceptionCaught(context, e.getCause());
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
