package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.mmdp.protocol.TaskProtos;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());

        pipeline.addLast(new ProtobufDecoder(TaskProtos.Task.getDefaultInstance()));
        pipeline.addLast(new ProtobufEncoder());

        pipeline.addLast(new TaskDecoder());

        // pipeline.addLast(new LoggingHandler(LogLevel.INFO));
        pipeline.addLast(new SortSpecDecoder());
        pipeline.addLast(new SortSpecEncoder());

        // pipeline.addLast(new LoggingHandler(LogLevel.INFO));
        pipeline.addLast(new MedianSpecDecoder());
        pipeline.addLast(new MedianSpecEncoder());

        pipeline.addLast(new MaxSpecDecoder());
        pipeline.addLast(new MaxSpecEncoder());

        // pipeline.addLast(new LoggingHandler(LogLevel.INFO));
        pipeline.addLast("SortTaskHandler", new SortTaskHandler());
        pipeline.addLast("MedianTaskHandler", new MedianTaskHandler());
        pipeline.addLast("MaxTaskHandler", new MaxTaskHandler());
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
