package io.guanghuizeng.fs.sync.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by guanghuizeng on 16/4/12.
 */
public class SyncMessageDecoder extends ByteToMessageDecoder {

    private enum State {
        Header, Body
    }

    private State state = State.Header;
    private int magic;
    private byte opCode;
    private String path;
    private long position;
    private ByteBuf content;
    private int pathLength;
    private long length;
    private int contentLength;

    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        switch (state) {
            case Header:
                // read header
                magic = in.readInt();
                opCode = in.readByte();
                pathLength = in.readInt();
                position = in.readLong();
                length = in.readLong();
                contentLength = in.readInt();

                state = State.Body;
            case Body:
                byte[] pathBytes = new byte[pathLength];
                in.readBytes(pathBytes);
                path = new String(pathBytes);

                content = Unpooled.buffer(contentLength);
                in.readBytes(content, contentLength);
                out.add(new SyncMessage(opCode, path, position, length, content));
                state = State.Header;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

