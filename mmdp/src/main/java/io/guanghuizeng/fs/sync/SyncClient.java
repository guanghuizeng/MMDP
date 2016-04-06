package io.guanghuizeng.fs.sync;

import io.guanghuizeng.fs.AbsoluteFilePath;
import io.netty.buffer.ByteBuf;

/**
 * Created by guanghuizeng on 16/4/6.
 */
public class SyncClient {

    public SyncClient(AbsoluteFilePath afp) {

    }
    public void push(ByteBuf cache) {

    }

    public ByteBuf poll(int size) {
        return null;
    }

    // 文件大小
    public long getLength() {
        return 0;
    }
}
