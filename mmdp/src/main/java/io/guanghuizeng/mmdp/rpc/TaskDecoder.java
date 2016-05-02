package io.guanghuizeng.mmdp.rpc;

import io.guanghuizeng.mmdp.protocol.TaskProtos;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * task -> sort or median
 */
public class TaskDecoder extends MessageToMessageDecoder<TaskProtos.Task> {

    public void decode(ChannelHandlerContext context, TaskProtos.Task in, List<Object> out) {

        TaskProtos.Opcode opcode = in.getOpcode();
        switch (opcode) {
            case SORT:
                out.add(in.getSortSubTask());
                break;
            case MEDIAN:
                out.add(in.getMedianSubTask());
                break;
            default:
                break; // TODO 做些什么...
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
