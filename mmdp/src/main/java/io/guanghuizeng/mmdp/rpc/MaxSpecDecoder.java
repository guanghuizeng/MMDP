package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.fs.ServiceID;
import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.fs.VirtualPath;
import io.guanghuizeng.mmdp.MaxSubTaskSpec;
import io.guanghuizeng.mmdp.protocol.TaskProtos;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * TaskProtos.MaxSubTask => MaxSubTaskSpec
 */
public class MaxSpecDecoder extends MessageToMessageDecoder<TaskProtos.MaxSubTask> {

    @Override
    protected void decode(ChannelHandlerContext ctx, TaskProtos.MaxSubTask msg, List<Object> out) throws Exception {
        out.add(new MaxSubTaskSpec(build(msg.getInput()), build(msg.getOutput()), msg.getCount()));
    }

    private Uri build(TaskProtos.Uri uri) {
        TaskProtos.ServiceID inId = uri.getId();
        TaskProtos.VirtualPath inPath = uri.getPath();

        return new Uri(new ServiceID(inId.getHost(), inId.getSyncPort(), inId.getEnginePort()),
                new VirtualPath(inPath.getPath()));
    }
}
