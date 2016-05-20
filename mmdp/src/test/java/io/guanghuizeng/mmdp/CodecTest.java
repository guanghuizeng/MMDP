package io.guanghuizeng.mmdp;

import io.guanghuizeng.fs.ServiceID;
import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.fs.VirtualPath;
import io.guanghuizeng.mmdp.algs2.Histogram;
import io.guanghuizeng.mmdp.protocol.TaskProtos;
import io.guanghuizeng.mmdp.rpc.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


/**
 * test
 */
public class CodecTest {

    @Test
    public void testSortTaskSpec() {

        EmbeddedChannel channel = new EmbeddedChannel(
                new ProtobufVarint32FrameDecoder(),
                new ProtobufVarint32LengthFieldPrepender(),

                new ProtobufDecoder(TaskProtos.Task.getDefaultInstance()),
                new ProtobufEncoder(),
                new TaskDecoder(),

                new SortSpecDecoder(),
                new SortSpecEncoder()
        );

        EmbeddedChannel channel1 = new EmbeddedChannel(
                new ProtobufVarint32FrameDecoder(),
                new ProtobufVarint32LengthFieldPrepender(),

                new ProtobufDecoder(TaskProtos.Task.getDefaultInstance()),
                new ProtobufEncoder(),
                new TaskDecoder(),

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

    @Test
    public void testMedianTaskSpecCodec() throws IOException {

        EmbeddedChannel channel = new EmbeddedChannel(
                new ProtobufVarint32FrameDecoder(),
                new ProtobufVarint32LengthFieldPrepender(),

                new ProtobufDecoder(TaskProtos.Task.getDefaultInstance()),
                new ProtobufEncoder(),
                new TaskDecoder(),

                new MedianSpecDecoder(),
                new MedianSpecEncoder()
        );

        EmbeddedChannel channel1 = new EmbeddedChannel(
                new ProtobufVarint32FrameDecoder(),
                new ProtobufVarint32LengthFieldPrepender(),

                new ProtobufDecoder(TaskProtos.Task.getDefaultInstance()),
                new ProtobufEncoder(),
                new TaskDecoder(),

                new MedianSpecDecoder(),
                new MedianSpecEncoder()
        );

        String input = "/data/data10m64";
        ServiceID service0 = new ServiceID("127.0.0.1", 8070, 8090);

        List<Long> data = Arrays.asList(1L, 2L, 3L, 4L);
        Histogram histogram = new Histogram(data, 9, 2, false);

        MedianSubTaskSpec subTaskSpec = MedianSubTaskSpec.build(
                new Uri(service0, new VirtualPath(input)),
                histogram, MedianPhase.FIRST,
                100, 0, 0, 0
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
        MedianSubTaskSpec outSpec = channel1.readInbound();

        assertEquals(subTaskSpec.getOpcode(), outSpec.getOpcode());
        assertEquals(subTaskSpec.getInput(), outSpec.getInput());
        assertEquals(subTaskSpec.getPhase(), outSpec.getPhase());
        assertEquals(subTaskSpec.getFirst(), outSpec.getFirst());
        assertEquals(subTaskSpec.getSecond(), outSpec.getSecond());
        assertEquals(subTaskSpec.getThird(), outSpec.getThird());
        assertEquals(subTaskSpec.getFourth(), outSpec.getFourth());
        assertEquals(subTaskSpec.getHistogram(), outSpec.getHistogram());

        assertEquals(data, subTaskSpec.getHistogram().toList());
        assertEquals(data, outSpec.getHistogram().toList());
    }
}
