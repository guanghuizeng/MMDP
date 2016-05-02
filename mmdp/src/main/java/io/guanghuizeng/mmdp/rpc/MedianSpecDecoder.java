package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.fs.ServiceID;
import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.fs.VirtualPath;
import io.guanghuizeng.mmdp.MedianPhase;
import io.guanghuizeng.mmdp.MedianSubTaskSpec;
import io.guanghuizeng.mmdp.algs2.Histogram;
import io.guanghuizeng.mmdp.protocol.TaskProtos;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * TaskProtos.MedianSubTask => MedianSubTaskSpec
 */
public class MedianSpecDecoder extends MessageToMessageDecoder<TaskProtos.MedianSubTask> {

    public void decode(ChannelHandlerContext context, TaskProtos.MedianSubTask in, List<Object> out) {

        TaskProtos.Uri inUri = in.getInput();
        TaskProtos.ServiceID inId = inUri.getId();
        TaskProtos.VirtualPath inPath = inUri.getPath();
        TaskProtos.Histogram inData = in.getHistogram();

        out.add(MedianSubTaskSpec.build
                (new Uri(new ServiceID(inId.getHost(), inId.getSyncPort(), inId.getEnginePort()),
                                new VirtualPath(inPath.getPath())),
                        build(inData),
                        build(in.getPhase()),
                        in.getFirst(),
                        in.getSecond(),
                        in.getThird(),
                        in.getFourth()
                ));
    }

    public MedianPhase build(TaskProtos.Phase phase) {
        switch (phase) {
            case FIRST:
                return MedianPhase.FIRST;
            case SECOND:
                return MedianPhase.SECOND;
            case THIRD:
                return MedianPhase.THIRD;
            case FOURTH:
                return MedianPhase.FOURTH;
            default:
                return MedianPhase.FIRST;
        }
    }

    public Histogram build(TaskProtos.Histogram data) {
        return new Histogram(
                data.getData().getNumberList(),
                data.getSize(),
                data.getBias(),
                data.getSigned());
    }
}
