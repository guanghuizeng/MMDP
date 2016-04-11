package io.guanghuizeng.fs.sync;

import io.guanghuizeng.fs.AbsoluteFilePath;
import io.guanghuizeng.fs.VirtualFile;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.HashMap;

/**
 * 负责调用 SyncClient, 同步数据
 * <p>
 * ???
 */
public class SyncService {
    /************************
     * fields
     ************************/

    /**
     * AFP - SyncClient
     */
    private HashMap<AbsoluteFilePath, SyncClient> cluster = new HashMap<>();

    /**
     * VirtualFile
     */
    private VirtualFile file;

    /************************
     * constructors
     ************************/

    public SyncService(VirtualFile file) {
        this.file = file;
        for (AbsoluteFilePath p : file.getAbsoluteFilePathList()) {
            cluster.put(p, new SyncClient());
        }
    }

    /************************
     * API
     ************************/

    /**
     * sync(SyncBuffer b)
     * <p>
     * b -> SyncAttr
     * 根据SyncAttr.AFP, 选择合适的SC
     */
    public SyncService sync(SyncBuffer buffer) {
        // push.
        for (int i = 0; i < buffer.numComponents(); i++) {
            SyncAttr a = buffer.getSyncAttr(i);
            cluster.get(a.getPath()).push(buffer.component(i), a);
        }
        return this;
    }

    /**
     * next
     */
    public SyncBuffer next(SyncBuffer buffer) {

        SyncBuffer newBuffer = file.next();
        // 清空 buffer; 添加 component 到 buffer 所指对象
        buffer.removeComponents(0, buffer.numComponents());
        buffer.clear();
        for (int i = 0; i < newBuffer.numComponents(); i++) {
            SyncAttr a = newBuffer.getSyncAttr(i);
            ByteBuf b = Unpooled.buffer();
            if (file.isRead()) {
                b = cluster.get(a.getPath()).poll(a);  // 同步数据
            }
            buffer.addComponent(b, a);
            buffer.writerIndex(buffer.writerIndex() + b.readableBytes());
        }

        return buffer;
    }
}
