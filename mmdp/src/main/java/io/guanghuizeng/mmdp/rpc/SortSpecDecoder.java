package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.fs.ServiceID;
import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.fs.VirtualPath;
import io.guanghuizeng.mmdp.SortSubTaskSpec;
import io.guanghuizeng.mmdp.protocol.TaskProtos;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 从message生成task spec
 */
public class SortSpecDecoder extends MessageToMessageDecoder<TaskProtos.SortSubTask> {

    public void decode(ChannelHandlerContext context, TaskProtos.SortSubTask in, List<Object> out) {
        out.add(build(in));
    }

    public SortSubTaskSpec build(TaskProtos.SortSubTask task) {

        TaskProtos.Uri inUri = task.getInput();
        TaskProtos.ServiceID inId = inUri.getId();
        TaskProtos.VirtualPath inPath = inUri.getPath();

        TaskProtos.Uri outUri = task.getOutput();
        TaskProtos.ServiceID outId = outUri.getId();
        TaskProtos.VirtualPath outPath = outUri.getPath();

        return new SortSubTaskSpec(
                new Uri(new ServiceID(inId.getHost(), inId.getSyncPort(), inId.getEnginePort()),
                        new VirtualPath(inPath.getPath())),
                new Uri(new ServiceID(outId.getHost(), outId.getSyncPort(), outId.getEnginePort()),
                        new VirtualPath(outPath.getPath())));
    }
}