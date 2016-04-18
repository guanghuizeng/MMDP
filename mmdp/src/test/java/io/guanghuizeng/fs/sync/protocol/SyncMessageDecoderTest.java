package io.guanghuizeng.fs.sync.protocol;

import io.guanghuizeng.fs.sync.protocol.Opcode;
import io.guanghuizeng.fs.sync.protocol.SyncMessage;
import io.guanghuizeng.fs.sync.protocol.SyncMessageDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by guanghuizeng on 16/4/12.
 */
public class SyncMessageDecoderTest {

    @Test
    public void test0() {

        /****************** encode ******************/

        int magic = 0x90;
        byte opCode = Opcode.WRITE;
        byte[] path = "/usr1/file1".getBytes();
        long position = 1024;
        byte[] content = "Great!".getBytes();

        ByteBuf buffer = Unpooled.buffer();

        buffer.writeInt(magic);
        buffer.writeByte(opCode);
        buffer.writeInt(path.length);
        buffer.writeLong(position);
        buffer.writeInt(content.length);

        buffer.writeBytes(path);
        buffer.writeBytes(content);

        /****************** decode ******************/

        EmbeddedChannel ch1 = new EmbeddedChannel(new SyncMessageDecoder());

        // write bytes
        assertTrue(ch1.writeInbound(buffer));
        assertTrue(ch1.finish());

        // read messages
        SyncMessage decoded = ch1.readInbound();

        assertEquals(magic, decoded.magic());
        assertEquals(opCode, decoded.opCode());
        assertEquals(new String(path), decoded.path());
        assertEquals(position, decoded.position());
        assertEquals(Unpooled.copiedBuffer(content), decoded.content());

        ch1.close();
    }
}
