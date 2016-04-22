package io.guanghuizeng.fs.output;


import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.fs.VirtualPath;
import io.guanghuizeng.fs.sync.SyncAttr;

import java.util.ArrayList;
import java.util.List;

/**
 * 可写文件
 */
public class WritableVirtualFile {


    /************************
     * fields
     ************************/
    /**
     * 一个 VirtualFile 包含多个 File
     */
    private List<File> fileList = new ArrayList<>();
    private int present = 0;

    private int bufferSize = 64;
    /**
     * URI
     */
    private List<Uri> uriList = new ArrayList<>();

    /************************
     * constructors
     ************************/

    public WritableVirtualFile(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public WritableVirtualFile(VirtualPath path, int bufferSize) {
        this.bufferSize = bufferSize;

    }

    /************************
     * API
     ************************/
    public SyncOutputBuffer next() {
        if (present < fileList.size()) {
            File file = fileList.get(present);
            file.decreaseWritableBytes(bufferSize);
            SyncAttr syncAttr = new SyncAttr(file.path);
            if (file.writableBytes < 0) {
                present++;
            }
            return new SyncOutputBuffer(bufferSize, syncAttr);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * add file
     */

    public void addFile(Uri path, long length) {
        uriList.add(path);
        fileList.add(new File(path, length));
    }

    private class File {
        private Uri path;
        private long length;
        private long writableBytes;

        public File(Uri path, long length) {
            this.path = path;
            this.length = length;
            this.writableBytes = length;
        }

        public void decreaseWritableBytes(int value) {
            writableBytes = writableBytes - value;
        }
    }

    public List<Uri> getUriList() {
        return uriList;
    }

    public int getBufferSize() {
        return bufferSize;
    }
}
