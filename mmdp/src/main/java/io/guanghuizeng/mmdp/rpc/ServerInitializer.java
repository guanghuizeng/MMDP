package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.mmdp.EngineBackendExecutor;
import io.guanghuizeng.mmdp.protocol.TaskProtos;
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

    private EngineBackendExecutor executor;

    public ServerInitializer(EngineBackendExecutor executor) {
        super();
        this.executor = executor;
    }

    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());

        pipeline.addLast(new ProtobufDecoder(TaskProtos.Task.getDefaultInstance()));
        pipeline.addLast(new ProtobufEncoder());

        pipeline.addLast(new TaskDecoder());

        pipeline.addLast(new SortSpecDecoder());
        pipeline.addLast(new SortSpecEncoder());

        pipeline.addLast(new MedianSpecDecoder());
        pipeline.addLast(new MedianSpecEncoder());

        pipeline.addLast(new MaxSpecDecoder());
        pipeline.addLast(new MaxSpecEncoder());

        pipeline.addLast(new ExistSpecDecoder());
        pipeline.addLast(new ExistSpecEncoder());

        pipeline.addLast(new TopSpecDecoder());
        pipeline.addLast(new TopSpecEncoder());

        pipeline.addLast(new SortTaskServerHandler(executor));
        pipeline.addLast(new MedianTaskServerHandler(executor));
        pipeline.addLast(new MaxTaskServerHandler(executor));
        pipeline.addLast(new ExistTaskServerHandler(executor));
        pipeline.addLast(new TopTaskServerHandler(executor));
    }
}
