package io.guanghuizeng.fs.sync.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

/**
 * Created by guanghuizeng on 16/4/12.
 */
public class SyncMessageEncoder extends MessageToByteEncoder<SyncMessage> {

    public void encode(ChannelHandlerContext ctx, SyncMessage msg, ByteBuf out) {
        byte[] path = msg.path().getBytes(CharsetUtil.UTF_8);

        // header
        out.writeInt(msg.magic());
        out.writeByte(msg.opCode());
        out.writeInt(path.length);
        out.writeLong(msg.position());
        out.writeLong(msg.length());
        out.writeInt(msg.content().readableBytes());

        // body
        out.writeBytes(path);
        out.writeBytes(msg.content());
    }
}