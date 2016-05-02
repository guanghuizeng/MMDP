package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.mmdp.MedianSubTaskSpec;
import io.guanghuizeng.mmdp.MedianTaskSpec;
import io.guanghuizeng.mmdp.SortSubTaskSpec;

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
    private ChannelPipeline pipeline;
    private SortTaskHandler sortTaskHandler;
    private MedianTaskHandler medianTaskHandler;

    public Client() {
    }

    public void startHelper() throws Exception {
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ClientInitializer());
        pipeline = b.connect(HOST, PORT).sync().channel().pipeline();
        sortTaskHandler = pipeline.get(SortTaskHandler.class);
        medianTaskHandler = pipeline.get(MedianTaskHandler.class);
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

    /**
     * 远程调用的设计: client端和server端都针对spec设计. 封装spec的网络传输过程.
     */

    public SortSubTaskSpec sort(SortSubTaskSpec spec) throws InterruptedException {
        return sortTaskHandler.exec(spec);
    }

    public MedianSubTaskSpec median(MedianSubTaskSpec spec) throws InterruptedException {
        return medianTaskHandler.exec(spec);
    }

}

