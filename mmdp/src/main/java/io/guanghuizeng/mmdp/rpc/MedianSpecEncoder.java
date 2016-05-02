package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.mmdp.MedianPhase;
import io.guanghuizeng.mmdp.MedianSubTaskSpec;
import io.guanghuizeng.mmdp.algs2.Histogram;
import io.guanghuizeng.mmdp.protocol.TaskProtos;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * TaskProtos.MedianSubTask <= MedianSubTaskSpec
 */
public class MedianSpecEncoder extends MessageToMessageEncoder<MedianSubTaskSpec> {

    public void encode(ChannelHandlerContext context, MedianSubTaskSpec in, List<Object> out) {

        TaskProtos.MedianSubTask.Builder builder = TaskProtos.MedianSubTask.newBuilder();

        builder.setOpcode(TaskProtos.Opcode.MEDIAN);
        builder.setInput(build(in.getInput()));
        builder.setPhase(build(in.getPhase()));
        builder.setFirst(in.getFirst());
        builder.setSecond(in.getSecond());
        builder.setThird(in.getThird());
        builder.setFourth(in.getFourth());

        if (in.getHistogram() != null){
            builder.setHistogram(build(in.getHistogram()));
        }

        TaskProtos.Task.Builder task = TaskProtos.Task.newBuilder();
        task.setOpcode(TaskProtos.Opcode.MEDIAN);
        task.setMedianSubTask(builder);

        out.add(task.build());
    }

    public TaskProtos.Uri build(Uri uri) {

        TaskProtos.Uri.Builder builder = TaskProtos.Uri.newBuilder();
        TaskProtos.ServiceID.Builder id = TaskProtos.ServiceID.newBuilder();
        TaskProtos.VirtualPath.Builder path = TaskProtos.VirtualPath.newBuilder();

        path.setPath(uri.getActualPath().toString());

        id.setHost(uri.getServiceID().getHost());
        id.setSyncPort(uri.getServiceID().getSyncPort());
        id.setEnginePort(uri.getServiceID().getEnginePort());

        builder.setPrefix(uri.getPrefix());
        builder.setInfix(uri.getInfix());
        builder.setId(id);
        builder.setPath(path);
        return builder.build();
    }

    public TaskProtos.Histogram build(Histogram histogram) {

        if (histogram != null) {
            TaskProtos.Histogram.Builder builder = TaskProtos.Histogram.newBuilder();
            TaskProtos.ListOfLong.Builder data = TaskProtos.ListOfLong.newBuilder();
            data.addAllNumber(histogram.toList());
            builder.setData(data);
            builder.setSize(histogram.getSize());
            builder.setBias(histogram.getBias());
            builder.setSigned(histogram.isSigned());
            return builder.build();
        } else {
            return null;
        }
    }

    public TaskProtos.Phase build(MedianPhase phase) {
        switch (phase) {
            case FIRST:
                return TaskProtos.Phase.FIRST;
            case SECOND:
                return TaskProtos.Phase.SECOND;
            case THIRD:
                return TaskProtos.Phase.THIRD;
            case FOURTH:
                return TaskProtos.Phase.FOURTH;
            default:
                return TaskProtos.Phase.FIRST;
        }
    }

}
