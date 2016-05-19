package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.fs.ServiceID;
import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.fs.VirtualPath;
import io.guanghuizeng.mmdp.TopSubTaskSpec;
import io.guanghuizeng.mmdp.protocol.TaskProtos;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TaskProtos.TopSubTask => TopSubTaskSpec
 */
public class TopSpecDecoder extends MessageToMessageDecoder<TaskProtos.TopSubTask> {

    @Override
    protected void decode(ChannelHandlerContext ctx, TaskProtos.TopSubTask msg, List<Object> out) throws Exception {
        out.add(TopSubTaskSpec.build(build(msg.getInput()),
                msg.getLowBound(), msg.getUpBound(), msg.getK(), build(msg.getResult())));
    }

    private Uri build(TaskProtos.Uri uri) {
        TaskProtos.ServiceID inId = uri.getId();
        TaskProtos.VirtualPath inPath = uri.getPath();

        return new Uri(new ServiceID(inId.getHost(), inId.getSyncPort(), inId.getEnginePort()),
                new VirtualPath(inPath.getPath()));
    }

    private Map<Long, Long> build(TaskProtos.TopSubTask.Map data) {

        Map<Long, Long> result = new HashMap<>();
        if (data.isInitialized()) {
            for (TaskProtos.TopSubTask.MapEntry entry : data.getEntryList()) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
}
