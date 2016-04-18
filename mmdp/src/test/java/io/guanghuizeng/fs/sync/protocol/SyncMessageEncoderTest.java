package io.guanghuizeng.fs.sync.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by guanghuizeng on 16/4/12.
 */
public class SyncMessageEncoderTest {

    @Test
    public void testEncoder() {

        byte[] data = "Great!".getBytes();

        ByteBuf dataBuf = Unpooled.copiedBuffer(data);
        SyncMessage msg = new SyncMessage(Opcode.WRITE, "/user1/file1", 1024, dataBuf);

        EmbeddedChannel ch = new EmbeddedChannel(new SyncMessageEncoder());
        assertTrue(ch.writeOutbound(msg));

        ByteBuf encoded = (ByteBuf) ch.readOutbound();

        assertNotNull(encoded);

        /******* 1 *******/
        assertEquals(msg.magic(), encoded.readInt());

        /******* 2 *******/
        assertEquals(msg.opCode(), encoded.readByte());

        /******* 3 *******/
        // length of path
        int pathLen = encoded.readInt();
        assertEquals(msg.path().length(), pathLen);

        /******* 4 *******/
        assertEquals(msg.position(), encoded.readLong());

        /******* 5 *******/
        // length of content
        int contentLen = encoded.readInt();
        assertEquals(data.length, contentLen);


        /******* 6 *******/
        // path
        byte[] path = new byte[pathLen];
        encoded.readBytes(path);
        assertArrayEquals("/user1/file1".getBytes(CharsetUtil.UTF_8), path);

        /******* 7 *******/
        // content
        byte[] encodedContent = new byte[contentLen];
        encoded.readBytes(encodedContent);
        assertArrayEquals(data, encodedContent);

    }

}
