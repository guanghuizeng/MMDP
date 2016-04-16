package io.guanghuizeng.fs.sync.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by guanghuizeng on 16/4/15.
 */
public class SyncMessageFrameDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf in, List<Object> out) {
        in.markReaderIndex();
        int preIndex = in.readerIndex();
        int length = in.readInt();

        if(length == preIndex) {
            return;
        }
        assert length >= 0;

        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        } else {
            out.add(in.readBytes(length));
            return;
        }
    }
}
