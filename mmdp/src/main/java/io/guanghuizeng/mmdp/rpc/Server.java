package io.guanghuizeng.mmdp.rpc;


import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.mmdp.EngineBackendExecutor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;

/**
 * Created by guanghuizeng on 16/4/2.
 */
public class Server {

    // config
    private int PORT = Integer.parseInt(System.getProperty("port", "8090"));
    private String HOME = System.getProperty("user.home").concat("/mmdpfs/");

    // init
    ServerBootstrap b = new ServerBootstrap();
    EventLoopGroup boss = new NioEventLoopGroup();
    EventLoopGroup worker = new NioEventLoopGroup();

    public Server() {
    }

    public Server(String homePath) {
        HOME = HOME.concat(homePath);
    }

    private void startHelper(int port) throws Exception {
        try {
            b.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ServerInitializer(new EngineBackendExecutor()))
                    .childAttr(AttributeKey.newInstance("FileSystem"), new FileSystem(HOME));
            b.bind(PORT).sync().channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    /************
     * API
     ************/
    public void start() throws Exception {
        startHelper(PORT);
    }

    public void start(int port) throws Exception {
        PORT = port;
        startHelper(PORT);
    }
}

