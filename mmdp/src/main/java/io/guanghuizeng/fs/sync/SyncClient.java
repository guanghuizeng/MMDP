package io.guanghuizeng.fs.sync;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 与 SyncClient 交互, 传输数据
 */
public class SyncClient {

    private static long number = 0;

    /**
     * @param buf  数据内容
     * @param attr 对数据的描述
     */
    public void push(ByteBuf buf, SyncAttr attr) {
        // 根据 attr, 将 buf 保存到相应位置
    }

    /**
     * @param attr 说明了从哪个文件的哪个位置读取信息.
     * @return
     */
    public ByteBuf poll(SyncAttr attr) {
        // attr -> SyncMessage
        // send SM
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < attr.getLast() - attr.getFirst(); i++) {
            buf.writeLong(number);
            number++;
        }
        return buf;
    }
}
