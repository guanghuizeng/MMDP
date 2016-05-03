package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.mmdp.EngineBackendExecutor;
import io.guanghuizeng.mmdp.MaxSubTaskSpec;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;

/**
 * 处理 Max Task
 */
public class MaxTaskServerHandler extends SimpleChannelInboundHandler<MaxSubTaskSpec> {

    private EngineBackendExecutor executor;

    public MaxTaskServerHandler(EngineBackendExecutor executor) {
        super(true);
        this.executor = executor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MaxSubTaskSpec msg) throws Exception {
        try {
            ctx.writeAndFlush(executor.exec(msg));
        } catch (IOException e) {
            exceptionCaught(ctx, e.getCause());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
