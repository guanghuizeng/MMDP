package io.guanghuizeng.mmdp;

import io.guanghuizeng.fs.ServiceID;
import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.fs.VirtualPath;
import io.guanghuizeng.mmdp.protocol.TaskProtos;
import io.guanghuizeng.mmdp.rpc.SortSpecDecoder;
import io.guanghuizeng.mmdp.rpc.SortSpecEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Created by guanghuizeng on 16/5/1.
 */
public class CodecTest {

    @Test
    public void test0() {

        EmbeddedChannel channel = new EmbeddedChannel(
                new ProtobufVarint32FrameDecoder(),
                new ProtobufVarint32LengthFieldPrepender(),

                new ProtobufDecoder(TaskProtos.SortSubTask.getDefaultInstance()),
                new ProtobufEncoder(),

                new SortSpecDecoder(),
                new SortSpecEncoder()
        );

        EmbeddedChannel channel1 = new EmbeddedChannel(
                new ProtobufVarint32FrameDecoder(),
                new ProtobufVarint32LengthFieldPrepender(),

                new ProtobufDecoder(TaskProtos.SortSubTask.getDefaultInstance()),
                new ProtobufEncoder(),

                new SortSpecDecoder(),
                new SortSpecEncoder()
        );


        String input = "/data/data10m64";
        String output = "/data/data10m64Sorted";

        ServiceID service0 = new ServiceID("127.0.0.1", 8070, 8090);
        ServiceID service1 = new ServiceID("127.0.0.1", 8071, 8091);

        SortSubTaskSpec subTaskSpec = new SortSubTaskSpec(
                new Uri(service0, new VirtualPath(input)),
                new Uri(service1, new VirtualPath(output))
        );


        // write
        assertTrue(channel.writeOutbound(subTaskSpec));
        assertTrue(channel.finish());

        // read
        ByteBuf out = channel.readOutbound();

        // write
        assertTrue(channel1.writeInbound(out));
        assertTrue(channel1.finish());

        // read
        SortSubTaskSpec outSpec = channel1.readInbound();

        assertEquals(subTaskSpec, outSpec);
    }
}
