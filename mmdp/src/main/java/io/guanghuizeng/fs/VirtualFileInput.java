package io.guanghuizeng.fs;

import io.guanghuizeng.fs.VirtualFile;
import io.guanghuizeng.fs.sync.SyncBuffer;
import io.guanghuizeng.fs.sync.SyncService;

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
    public VirtualFileInput(VirtualFile file) {
        this.file = file;
        syncService = new SyncService(file);
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
