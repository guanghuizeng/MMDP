package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.mmdp.ExistSubTaskSpec;
import io.guanghuizeng.mmdp.protocol.TaskProtos;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;
import java.util.Map;

/**
 * TaskProtos.ExistSubTask <= ExistSubTaskSpec
 */
public class ExistSpecEncoder extends MessageToMessageEncoder<ExistSubTaskSpec> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ExistSubTaskSpec msg, List<Object> out) throws Exception {

        TaskProtos.ExistSubTask.Builder builder = TaskProtos.ExistSubTask.newBuilder();

        builder.setOpcode(TaskProtos.Opcode.EXIST);
        builder.setInput(build(msg.getInput()));

        TaskProtos.ListOfLong.Builder data = TaskProtos.ListOfLong.newBuilder();
        data.addAllNumber(msg.getData());
        builder.setData(data);
        builder.setFpp(msg.getFpp());

        if (msg.getResult() != null) {
            builder.setResult(build(msg.getResult()));
        }

        TaskProtos.Task.Builder task = TaskProtos.Task.newBuilder();
        task.setOpcode(TaskProtos.Opcode.EXIST);
        task.setExistenceSubTask(builder);

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

    private TaskProtos.ExistSubTask.Map build(Map<Long, Boolean> data) {

        TaskProtos.ExistSubTask.Map.Builder result = TaskProtos.ExistSubTask.Map.newBuilder();
        for (Map.Entry<Long, Boolean> entry : data.entrySet()) {
            TaskProtos.ExistSubTask.MapEntry.Builder builder = TaskProtos.ExistSubTask.MapEntry.newBuilder();
            builder.setKey(entry.getKey());
            builder.setValue(entry.getValue());
            result.addEntry(builder);
        }

        return result.build();
    }
}
