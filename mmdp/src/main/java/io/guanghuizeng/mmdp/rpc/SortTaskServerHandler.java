package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.mmdp.EngineBackendExecutor;
import io.guanghuizeng.mmdp.SortSubTaskSpec;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;

/**
 * 处理业务
 */
public class SortTaskServerHandler extends SimpleChannelInboundHandler<SortSubTaskSpec> {

    private ChannelHandlerContext context;
    private EngineBackendExecutor executor;

    public SortTaskServerHandler(EngineBackendExecutor executor) {
        this.executor = executor;
    }

    public void channelRegistered(ChannelHandlerContext ctx) {
        context = ctx;
    }

    public void channelRead0(ChannelHandlerContext context, SortSubTaskSpec msg) {
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