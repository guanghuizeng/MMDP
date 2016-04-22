package io.guanghuizeng.fs.input;

import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.fs.sync.SyncAttr;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ??????
 * <p>
 * 分成哪些部分?
 */
public class VirtualFile {

    /************************
     * fields
     ************************/
    /**
     * 一个 VirtualFile 包含多个 File
     */
    private List<File> fileList = new ArrayList<>();

    private long bufferSize = 1024 * 1024;
    private int startFileIndex = 0;

    /**
     * URI
     */
    private List<Uri> uriList = new ArrayList<>();

    /************************
     * constructors
     ************************/
    public VirtualFile(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public VirtualFile(Uri... uris) {
        uriList.addAll(Arrays.asList(uris));
    }

    public VirtualFile(long bufferSize, Uri... uris) {
        uriList.addAll(Arrays.asList(uris));
    }


    /************************
     * API
     ************************/

    /**
     * next - 生成新的 SyncBuffer, 其中包含SyncInfo, 没有数据. 用SyncService来获取数据.
     */
    public SyncBuffer next() {

        SyncBuffer syncBuffer = new SyncBuffer();
        int endFileIndex = fileList.size();
        long sum = 0;
        int count = 0;

        for (int i = startFileIndex; i < endFileIndex; i++) {
            count++;
            File f = fileList.get(i);
            long pre = sum;
            sum = sum + f.available();
            if (sum >= bufferSize) {
                // 添加到 syncBuffer
                syncBuffer.addComponent(Unpooled.buffer(),
                        new SyncAttr(f.path, f.readIndex, bufferSize - pre));
                // 更新文件信息
                f.readIndex = f.readIndex + (bufferSize - pre);
                break;
            } else {
                // 添加到 syncBuffer
                syncBuffer.addComponent(Unpooled.buffer(),
                        new SyncAttr(f.path, f.readIndex, f.length));
                // 更新文件信息
                f.readIndex = f.length;
            }
        }
        // 更新文件信息
        startFileIndex = startFileIndex + (count - 1);
        return syncBuffer;
    }

    /************************
     * Others
     ************************/

    /**
     * 文件描述信息
     *
     * @param uri
     * @param readIndex
     * @param length
     */
    public void addFile(Uri uri, long readIndex, long length) {
        fileList.add(new File(uri, readIndex, length));
        uriList.add(uri);
    }

    public void initFile(Uri uri, long readIndex, long length) {
        fileList.add(new File(uri, readIndex, length));
    }

    public boolean isRead() {
        return true;
    }

    public List<Uri> getUriList() {
        return uriList;
    }

    /**
     * 内部数据结构, 描述远程文件的信息
     */
    private class File {
        private Uri path;

        private long readIndex;
        private long length;

        public File(Uri path, long readIndex, long length) {
            this.path = path;
            this.readIndex = readIndex;
            this.length = length;
        }

        public long available() {
            return length - readIndex;
        }
    }
}
