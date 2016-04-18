package io.guanghuizeng.mmdp;

import io.guanghuizeng.mmdp.protocol.MmdpProtos.Message.Function;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by guanghuizeng on 16/4/2.
 */
public class Client {


    /************
     * config: server host, server port. 默认为本机
     ************/
    private String HOST = System.getProperty("host", "127.0.0.1");
    private int PORT = Integer.parseInt(System.getProperty("port", "8090"));

    /************
     * init
     ************/
    private Bootstrap b = new Bootstrap();
    private EventLoopGroup group = new NioEventLoopGroup();
    private ClientHandler handler;

    public Client() {
    }

    public void startHelper() throws Exception {
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ClientInitializer());
        ChannelPipeline pipeline = b.connect(HOST, PORT).sync().channel().pipeline();
        handler = pipeline.get(ClientHandler.class);
    }

    /************
     * API
     ************/

    public Client start() throws Exception {
        startHelper();
        return this;
    }

    public Client start(String host, int port) throws Exception {
        HOST = host;
        PORT = port;
        startHelper();
        return this;
    }

    public String echo() throws Exception {
        return handler.execute(Function.ECHO, "", "", "", "");
    }

    public String sort(String source, String target) throws Exception {
        return handler.execute(Function.SORT, source, target, "", "");
    }


}

