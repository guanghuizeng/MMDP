package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.mmdp.MaxSubTaskSpec;
import io.guanghuizeng.mmdp.protocol.TaskProtos;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * TaskProtos.Task <= TaskProtos.MedianSubTask <= MedianSubTaskSpec
 */
public class MaxSpecEncoder extends MessageToMessageEncoder<MaxSubTaskSpec> {

    public void encode(ChannelHandlerContext context, MaxSubTaskSpec in, List<Object> out) {

        TaskProtos.MaxSubTask.Builder builder = TaskProtos.MaxSubTask.newBuilder();

        builder.setOpcode(TaskProtos.Opcode.MAX);
        builder.setInput(build(in.getInput()));
        builder.setOutput(build(in.getOutput()));
        builder.setCount(in.getCount());

        TaskProtos.Task.Builder task = TaskProtos.Task.newBuilder();
        task.setOpcode(TaskProtos.Opcode.MAX);
        task.setMaxSubTask(builder);

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
}
