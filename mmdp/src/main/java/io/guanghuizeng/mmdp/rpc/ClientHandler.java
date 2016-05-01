package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.mmdp.protocol.MmdpProtos.Message;
import io.guanghuizeng.mmdp.protocol.MmdpProtos.Message.Function;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by guanghuizeng on 16/4/2.
 */
public class ClientHandler extends SimpleChannelInboundHandler<Message> {

    BlockingQueue<Message> answers = new LinkedBlockingQueue<Message>();
    ChannelHandlerContext context;

    /************
     * IO
     ************/
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        answers.add(msg);
    }

    public void channelRegistered(ChannelHandlerContext ctx) {
        context = ctx;
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private void send(Message msg) {
        context.writeAndFlush(msg);
    }

    /************
     * API
     ************/
    public String execute(Function function, String source, String target, String data, String info)
            throws InterruptedException {

        // build msg
        Message.Builder builder = Message.newBuilder();

        builder.setFunction(function);
        builder.setSource(source);
        builder.setTarget(target);
        builder.setData(data);
        builder.setData(info);

        Message msg = builder.build();

        // send to server
        send(msg);

        // get result
        Message result = answers.take(); // TODO: 改善

        // 返回结果
        return result.getInfo();
    }



}
