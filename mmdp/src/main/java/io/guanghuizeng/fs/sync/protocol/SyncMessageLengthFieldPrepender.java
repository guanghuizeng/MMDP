package io.guanghuizeng.fs.sync.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 添加 length 到 packet,  解决 TCP 拆包问题
 */
public class SyncMessageLengthFieldPrepender extends MessageToByteEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext context, ByteBuf msg, ByteBuf out) {

        int bodyLen = msg.readableBytes();
        int headerLen = Integer.BYTES;
        out.ensureWritable(headerLen + bodyLen);
        out.writeInt(bodyLen);
        out.writeBytes(msg, msg.readerIndex(), bodyLen);
    }
}