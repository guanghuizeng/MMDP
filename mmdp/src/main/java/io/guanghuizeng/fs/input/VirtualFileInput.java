package io.guanghuizeng.fs.input;

import io.guanghuizeng.fs.sync.SyncBuffer;

/**
 * Created by guanghuizeng on 16/4/11.
 */
public class VirtualFileInput {

    /**
     *
     */
    private SyncBuffer buffer = new SyncBuffer();
    private SyncService syncService;
    private VirtualFile file;

    /*************************
     * Constructors
     *************************/
    public VirtualFileInput(VirtualFile file) throws InterruptedException {
        this.file = file;
        syncService = new SyncService(file);
        syncService.next(buffer);
    }

    /*************************
     * API
     *************************/

    public long readLong() {
        if (buffer.readableBytes() >= Long.BYTES) {
            long output = buffer.readLong();
            if (buffer.readableBytes() < Long.BYTES) {
                syncService.next(buffer);
            }
            return output;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }
}
