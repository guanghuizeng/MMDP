package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.mmdp.SortSubTaskSpec;
import io.guanghuizeng.mmdp.protocol.TaskProtos;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 从task spec生成message
 */
public class SortSpecEncoder extends MessageToMessageEncoder<SortSubTaskSpec> {

    public void encode(ChannelHandlerContext context, SortSubTaskSpec in, List<Object> out) {
        out.add(build(in));
    }

    /**
     * 从spec生成message
     *
     * @param spec
     * @return
     */
    public TaskProtos.SortSubTask build(SortSubTaskSpec spec) {

        TaskProtos.SortSubTask.Builder task = TaskProtos.SortSubTask.newBuilder();
        task.setOpcode(TaskProtos.SortSubTask.Opcode.SORT);
        task.setInput(build(spec.getInput()));
        task.setOutput(build(spec.getOutput()));

        return task.build();
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
