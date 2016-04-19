package io.guanghuizeng.fs.output;

import io.guanghuizeng.fs.sync.SyncAttr;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.buffer.UnpooledDirectByteBuf;

/**
 * Created by guanghuizeng on 16/4/12.
 */
public class SyncOutputBuffer extends UnpooledDirectByteBuf {


    /************************
     * fields
     ************************/

    /**
     * 保存对于文件的信息
     */
    private SyncAttr syncAttr;

    /************************
     * constructors
     ************************/

    public SyncOutputBuffer(int maxCapacity) {
        super(new UnpooledByteBufAllocator(true), 0, maxCapacity);
    }

    public SyncOutputBuffer(int bufferSize, SyncAttr attr) {
        super(new UnpooledByteBufAllocator(true), bufferSize, bufferSize);
        this.syncAttr = attr;
    }

    /************************
     * Additional APIs
     ************************/

    public SyncAttr getSyncAttr() {
        return syncAttr;
    }

    public void setSyncAttr(SyncAttr syncAttr) {
        this.syncAttr = syncAttr;
    }
}
