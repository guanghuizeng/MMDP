package io.guanghuizeng.mmdp;

import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.mmdp.algs2.ExternalSort;
import io.guanghuizeng.mmdp.algs.MaxFinder;
import io.guanghuizeng.mmdp.protocol.MmdpProtos.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

import java.io.IOException;
import java.util.Comparator;

/**
 * Created by guanghuizeng on 16/4/2.
 * TODO: 将path的类型更改为FilePath
 */
public class ExecutionHandler extends SimpleChannelInboundHandler<Message> {

    private final AttributeKey<FileSystem> fileSystemKey = AttributeKey.valueOf("FileSystem");
    private ChannelHandlerContext context;

    /************
     * Execution
     ************/
    private void sort(String source, String target) throws IOException {
        FileSystem fileSystem = context.attr(fileSystemKey).get();
        // source -> file; target -> file; ES.sort(src, dst)
        ExternalSort.sort(fileSystem.getPath(source), fileSystem.getPath(target), Comparator.naturalOrder());
    }

    private void max(String source, String target, int k) throws IOException {
        FileSystem fileSystem = context.attr(fileSystemKey).get();
        // source -> file; target -> file
        MaxFinder.find(fileSystem.getFile(source), fileSystem.getFile(target), k);
    }

    private Message execute(Message msg) throws IOException {
        Message.Builder builder = msg.toBuilder();
        switch (msg.getFunction()) {
            case ECHO:
                builder.setInfo("hi");
                break;
            case SORT:
                sort(msg.getSource(), msg.getTarget());
                break;
            case MAX:
                max(msg.getSource(), msg.getTarget(), 1000); // TODO: 修改
                break;
            // TODO: 增加
        }
        return builder.build();
    }

    /************
     * IO
     ************/
    public void channelRegistered(ChannelHandlerContext ctx) {
        context = ctx;

    }

    public void channelRead0(ChannelHandlerContext ctx, Message msg) throws IOException {
        ctx.writeAndFlush(execute(msg));
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
