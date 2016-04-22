package io.guanghuizeng.fs.input;

import io.guanghuizeng.fs.AbsoluteFilePath;
import io.guanghuizeng.fs.sync.SyncAttr;
import io.guanghuizeng.fs.sync.SyncClient;
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

    public SyncService(VirtualFile file) throws InterruptedException {
        this.file = file;
        for (AbsoluteFilePath p : file.getAbsoluteFilePathList()) {
            cluster.put(p, new SyncClient(p.getServiceID()));
        }
        // TODO 更新文件信息
        for (AbsoluteFilePath p : file.getAbsoluteFilePathList()) {
            SyncAttr attr = new SyncAttr(p);
            long length = cluster.get(p).length(attr);
            file.initFile(p, 0, length);
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
    public SyncService sync(SyncBuffer buffer) throws InterruptedException {
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
    public SyncBuffer next(SyncBuffer buffer) throws InterruptedException {

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


    /**
     * 到任务结束再 close. 如何实现?
     */
    public void close() throws InterruptedException {
        for (SyncClient cli : cluster.values()) {
            cli.close();
        }
    }
}
