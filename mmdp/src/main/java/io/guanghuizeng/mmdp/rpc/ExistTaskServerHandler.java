package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.mmdp.EngineBackendExecutor;
import io.guanghuizeng.mmdp.ExistSubTaskSpec;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;

/**
 * 处理 Exist Task
 */
public class ExistTaskServerHandler extends SimpleChannelInboundHandler<ExistSubTaskSpec> {

    private EngineBackendExecutor executor;

    public ExistTaskServerHandler(EngineBackendExecutor executor) {
        super(true);
        this.executor = executor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ExistSubTaskSpec msg) throws Exception {
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
