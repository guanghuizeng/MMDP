package io.guanghuizeng.fs.input;

import io.guanghuizeng.fs.sync.SyncAttr;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;

import java.util.HashMap;

/**
 * 扩展 CompositeByteBuf, 保存每个 Component 的同步信息.
 */
public class SyncBuffer extends CompositeByteBuf {


    /************************
     * fields
     ************************/

    /**
     * 保存 Components 对应到文件的信息
     */
    private HashMap<Integer, SyncAttr> SyncInfo = new HashMap<>();

    /************************
     * constructors
     ************************/

    public SyncBuffer() {
        super(new UnpooledByteBufAllocator(true), true, 1024);
    }

    /************************
     * Additional APIs
     ************************/

    public SyncBuffer addComponent(ByteBuf buffer, SyncAttr attr) {
        addComponent(buffer);
        int cIndex = this.numComponents() - 1;
        SyncInfo.put(cIndex, attr);
        return this;
    }

    public SyncAttr getSyncAttr(int cIndex) {
        return SyncInfo.get(cIndex);
    }
}
