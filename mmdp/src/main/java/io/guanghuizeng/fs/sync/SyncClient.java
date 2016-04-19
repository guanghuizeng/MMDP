package io.guanghuizeng.fs.sync;

import io.guanghuizeng.fs.Address;
import io.guanghuizeng.fs.sync.protocol.SyncMessageDecoder;
import io.guanghuizeng.fs.sync.protocol.SyncMessageEncoder;
import io.guanghuizeng.fs.sync.protocol.SyncMessageFrameDecoder;
import io.guanghuizeng.fs.sync.protocol.SyncMessageLengthFieldPrepender;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.compression.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 与 SyncClient 交互, 传输数据
 */
public class SyncClient {

    private String HOST = System.getProperty("host", "127.0.0.1");
    private int PORT = Integer.parseInt(System.getProperty("port", "8093"));

    private Bootstrap b = new Bootstrap();
    private EventLoopGroup group = new NioEventLoopGroup();
    private SyncClientHandler handler;

    public SyncClient() throws InterruptedException {
        this(new Address("127.0.0.1", 8093));
    }

    public SyncClient(Address address) throws InterruptedException {
        HOST = address.getHost();
        PORT = address.getPort();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_LINGER, 0)
                .handler(new ChannelInitializer<SocketChannel>() {
                    public void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();

                        // 非常耗费时间
                        // pipeline.addLast(new LoggingHandler(LogLevel.INFO));

                        pipeline.addLast(new JZlibDecoder());
                        pipeline.addLast(new JZlibEncoder());

                        // pipeline.addLast(new LoggingHandler(LogLevel.INFO));

                        pipeline.addLast(new SyncMessageFrameDecoder());
                        pipeline.addLast(new SyncMessageLengthFieldPrepender());

                        pipeline.addLast(new SyncMessageDecoder());
                        pipeline.addLast(new SyncMessageEncoder());

                        pipeline.addLast(new SyncClientHandler());
                    }
                });

        handler = b.connect(HOST, PORT).sync()
                .channel().pipeline().get(SyncClientHandler.class);
    }

    public void push(ByteBuf buf, SyncAttr attr) throws InterruptedException {
        handler.push(buf, attr);
    }

    public ByteBuf poll(SyncAttr attr) throws InterruptedException {
        return handler.pool(attr);
    }

    /**
     * 获取文件大小 (bytes)
     *
     * @return
     */
    public long length(SyncAttr attr) throws InterruptedException {
        return handler.length(attr);
    }

    public void close() throws InterruptedException {
        handler.close();
        group.shutdownGracefully();
    }
}
