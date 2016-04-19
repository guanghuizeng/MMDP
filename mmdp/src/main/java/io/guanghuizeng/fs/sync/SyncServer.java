package io.guanghuizeng.fs.sync;

import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.fs.sync.protocol.SyncMessageDecoder;
import io.guanghuizeng.fs.sync.protocol.SyncMessageEncoder;
import io.guanghuizeng.fs.sync.protocol.SyncMessageFrameDecoder;
import io.guanghuizeng.fs.sync.protocol.SyncMessageLengthFieldPrepender;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import io.netty.handler.codec.compression.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by guanghuizeng on 16/4/13.
 */
public class SyncServer {

    int PORT = 8093;

    private String HOME;
    private ServerBootstrap b = new ServerBootstrap();
    private EventLoopGroup boss = new NioEventLoopGroup();
    private EventLoopGroup worker = new NioEventLoopGroup();

    public SyncServer() {
    }

    public void start(int port, String home) throws InterruptedException {
        PORT = port;
        HOME = home;
        try {
            b.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel channel) {
                            ChannelPipeline pipeline = channel.pipeline();

                            // pipeline.addLast(new LoggingHandler(LogLevel.INFO));

                            pipeline.addLast(ZlibCodecFactory.newZlibDecoder());
                            pipeline.addLast(ZlibCodecFactory.newZlibEncoder(1));

                            // pipeline.addLast(new LoggingHandler(LogLevel.INFO));

                            pipeline.addLast(new SyncMessageFrameDecoder());
                            pipeline.addLast(new SyncMessageLengthFieldPrepender());


                            pipeline.addLast(new SyncMessageDecoder());
                            pipeline.addLast(new SyncMessageEncoder());

                            pipeline.addLast(new SyncServerHandler(HOME));
                        }
                    });
            b.bind(PORT).sync().channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
