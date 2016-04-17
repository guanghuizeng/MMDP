package io.guanghuizeng.fs.output;


import io.guanghuizeng.fs.AbsoluteFilePath;
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
     * AFP
     */
    private List<AbsoluteFilePath> absoluteFilePathList = new ArrayList<>();

    /************************
     * constructors
     ************************/

    public WritableVirtualFile(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public WritableVirtualFile(String path) {

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

    public void addFile(AbsoluteFilePath path, long length) {
        absoluteFilePathList.add(path);
        fileList.add(new File(path, length));
    }

    private class File {
        private AbsoluteFilePath path;
        private long length;
        private long writableBytes;

        public File(AbsoluteFilePath path, long length) {
            this.path = path;
            this.length = length;
            this.writableBytes = length;
        }

        public void decreaseWritableBytes(int value) {
            writableBytes = writableBytes - value;
        }
    }

    public List<AbsoluteFilePath> getAbsoluteFilePathList() {
        return absoluteFilePathList;
    }

    public int getBufferSize() {
        return bufferSize;
    }
}
