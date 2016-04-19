package io.guanghuizeng.fs.output;

/**
 * Created by guanghuizeng on 16/4/12.
 */
public class VirtualFileOutput {


    /**
     * Fields
     */
    private SyncOutputBuffer buffer;
    private SyncService syncService;
    private WritableVirtualFile file;

    /*************************
     * Constructors
     *************************/
    public VirtualFileOutput(WritableVirtualFile file) throws Exception {
        this.file = file;
        syncService = new SyncService(file);
        buffer = new SyncOutputBuffer(file.getBufferSize());
        syncService.next(buffer);
    }

    /*************************
     * API
     *************************/

    public void writeLong(long value) throws InterruptedException {
        if (buffer.writableBytes() >= Long.BYTES) {
            buffer.writeLong(value);
            if (buffer.writableBytes() < Long.BYTES) {
                syncService.sync(buffer).next(buffer);
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void close() throws InterruptedException {
        syncService.sync(buffer);
        syncService.close();
    }
}
