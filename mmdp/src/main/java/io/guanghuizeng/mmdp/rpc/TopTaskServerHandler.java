package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.mmdp.EngineBackendExecutor;
import io.guanghuizeng.mmdp.TopSubTaskSpec;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;

/**
 * Created by guanghuizeng on 16/5/6.
 */
public class TopTaskServerHandler extends SimpleChannelInboundHandler<TopSubTaskSpec> {

    private EngineBackendExecutor executor;

    public TopTaskServerHandler(EngineBackendExecutor executor) {
        super(true);
        this.executor = executor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TopSubTaskSpec msg) throws Exception {
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
