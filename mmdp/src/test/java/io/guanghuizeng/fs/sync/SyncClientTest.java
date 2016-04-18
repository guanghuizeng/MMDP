package io.guanghuizeng.fs.sync;

import io.guanghuizeng.fs.AbsoluteFilePath;
import io.guanghuizeng.fs.Address;
import io.guanghuizeng.fs.sync.protocol.Opcode;
import io.guanghuizeng.fs.sync.protocol.SyncMessageEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by guanghuizeng on 16/4/13.
 */
public class SyncClientTest {

    @Test
    public void test0() throws Exception {

        String HOST = System.getProperty("host", "127.0.0.1");
        int PORT = Integer.parseInt(System.getProperty("port", "8093"));
        SyncClient client = new SyncClient(new Address(HOST, PORT));

        ByteBuf data = Unpooled.buffer();
        for (int i = 0; i < 50000; i++) {
            data.writeLong(1024);
        }

        AbsoluteFilePath afp = new AbsoluteFilePath("/user1/file4");
        SyncAttr attr = new SyncAttr(afp, 0, 0);

        client.push(data, attr);
    }

    @Test
    public void testHandlers() throws InterruptedException {
        /** init channel **/
        SyncClientHandler handler = new SyncClientHandler();
        EmbeddedChannel channel = new EmbeddedChannel(new SyncMessageEncoder(), handler);

        /** push data **/
        ByteBuf data = Unpooled.buffer();
        for (int i = 0; i < 2000; i++) {
            data.writeLong(1024);
        }
        AbsoluteFilePath afp = new AbsoluteFilePath("/user1/file3");
        SyncAttr attr = new SyncAttr(afp, 0, 0);

        handler.push(data, attr);

        /** **/
        ByteBuf encoded = channel.readOutbound();

        assertEquals(0x90, encoded.readInt());
        assertEquals(Opcode.APPEND, encoded.readByte());
    }
}