package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.fs.ServiceID;
import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.fs.VirtualPath;
import io.guanghuizeng.mmdp.ExistSubTaskSpec;
import io.guanghuizeng.mmdp.protocol.TaskProtos;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * TaskProtos.ExistSubTask => ExistSubTaskSpec
 */
public class ExistSpecDecoder extends MessageToMessageDecoder<TaskProtos.ExistSubTask> {

    @Override
    protected void decode(ChannelHandlerContext ctx, TaskProtos.ExistSubTask msg, List<Object> out)
            throws Exception {

        out.add(ExistSubTaskSpec.build(build(msg.getInput()),
                new HashSet<>(msg.getData().getNumberList()),
                msg.getFpp(), build(msg.getResult())));
    }

    private Uri build(TaskProtos.Uri uri) {
        TaskProtos.ServiceID inId = uri.getId();
        TaskProtos.VirtualPath inPath = uri.getPath();

        return new Uri(new ServiceID(inId.getHost(), inId.getSyncPort(), inId.getEnginePort()),
                new VirtualPath(inPath.getPath()));
    }

    private Map<Long, Boolean> build(TaskProtos.ExistSubTask.Map data) {

        Map<Long, Boolean> result = new HashMap<>();
        if (data.isInitialized()) {
            for (TaskProtos.ExistSubTask.MapEntry entry : data.getEntryList()) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

}
