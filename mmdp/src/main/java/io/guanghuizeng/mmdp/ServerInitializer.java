package io.guanghuizeng.mmdp;

import io.guanghuizeng.mmdp.protocol.MmdpProtos.Message;

import io.netty.channel.socket.SocketChannel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * Created by guanghuizeng on 16/4/2.
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());

        pipeline.addLast(new ProtobufDecoder(Message.getDefaultInstance()));
        pipeline.addLast(new ProtobufEncoder());

        pipeline.addLast(new ExecutionHandler());
    }
}
