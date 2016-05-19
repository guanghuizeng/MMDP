package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.mmdp.TopSubTaskSpec;
import io.guanghuizeng.mmdp.protocol.TaskProtos;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;
import java.util.Map;

/**
 * * TaskProtos.TopSubTask <= TopSubTaskSpec
 */
public class TopSpecEncoder extends MessageToMessageEncoder<TopSubTaskSpec> {

    @Override
    protected void encode(ChannelHandlerContext ctx, TopSubTaskSpec msg, List<Object> out) throws Exception {

        TaskProtos.TopSubTask.Builder builder = TaskProtos.TopSubTask.newBuilder();
        builder.setOpcode(TaskProtos.Opcode.TOP);
        builder.setInput(build(msg.getInput()));
        builder.setLowBound(msg.getLowBound());
        builder.setUpBound(msg.getUpBound());
        builder.setK(msg.getK());
        if (msg.getResult() != null) {
            builder.setResult(build(msg.getResult()));
        }

        TaskProtos.Task.Builder task = TaskProtos.Task.newBuilder();
        task.setOpcode(TaskProtos.Opcode.TOP);
        task.setTopSubTask(builder);
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

    private TaskProtos.TopSubTask.Map build(Map<Long, Long> data) {
        TaskProtos.TopSubTask.Map.Builder result = TaskProtos.TopSubTask.Map.newBuilder();
        for (Map.Entry<Long, Long> entry : data.entrySet()) {
            TaskProtos.TopSubTask.MapEntry.Builder builder = TaskProtos.TopSubTask.MapEntry.newBuilder();
            builder.setKey(entry.getKey());
            builder.setValue(entry.getValue());
            result.addEntry(builder);
        }
        return result.build();
    }
}
