package io.guanghuizeng.fs.output;


import io.guanghuizeng.fs.AbsoluteFilePath;
import io.guanghuizeng.fs.sync.SyncAttr;
import io.guanghuizeng.fs.sync.SyncClient;


import java.util.HashMap;

/**
 * Created by guanghuizeng on 16/4/12.
 */
public class SyncService {

    /************************
     * fields
     ************************/
    /**
     * AFP - SyncClient
     */
    private HashMap<AbsoluteFilePath, SyncClient> cluster = new HashMap<>();

    private WritableVirtualFile file;

    /************************
     * constructors
     ************************/

    public SyncService(WritableVirtualFile file) throws Exception {
        this.file = file;
        for (AbsoluteFilePath p : file.getAbsoluteFilePathList()) {
            cluster.put(p, new SyncClient(p.getAddress()));
        }
    }

    /************************
     * API
     ************************/

    public SyncService sync(SyncOutputBuffer buffer) throws InterruptedException {
        SyncAttr a = buffer.getSyncAttr();
        cluster.get(a.getPath()).push(buffer, a);
        return this;
    }

    public void next(SyncOutputBuffer buffer) {
        SyncOutputBuffer newBuffer = file.next();
        buffer.clear();
        buffer.capacity(newBuffer.capacity());
        buffer.setSyncAttr(newBuffer.getSyncAttr());
    }

    public void close() throws InterruptedException {
        for (SyncClient cli : cluster.values()) {
            cli.close();
        }
    }
}
