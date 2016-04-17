package io.guanghuizeng.fs.input;

import io.guanghuizeng.fs.AbsoluteFilePath;
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
     * AFP
     */
    private List<AbsoluteFilePath> absoluteFilePathList = new ArrayList<>();

    /************************
     * constructors
     ************************/
    public VirtualFile(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public VirtualFile(AbsoluteFilePath... afps) {
        absoluteFilePathList.addAll(Arrays.asList(afps));
    }

    public VirtualFile(long bufferSize, AbsoluteFilePath... afps) {
        absoluteFilePathList.addAll(Arrays.asList(afps));
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
     * @param afp
     * @param readIndex
     * @param length
     */
    public void addFile(AbsoluteFilePath afp, long readIndex, long length) {
        fileList.add(new File(afp, readIndex, length));
        absoluteFilePathList.add(afp);
    }

    public void initFile(AbsoluteFilePath afp, long readIndex, long length) {
        fileList.add(new File(afp, readIndex, length));
    }

    public boolean isRead() {
        return true;
    }

    public List<AbsoluteFilePath> getAbsoluteFilePathList() {
        return absoluteFilePathList;
    }

    /**
     * 内部数据结构, 描述远程文件的信息
     */
    private class File {
        private AbsoluteFilePath path;

        private long readIndex;
        private long length;

        public File(AbsoluteFilePath path, long readIndex, long length) {
            this.path = path;
            this.readIndex = readIndex;
            this.length = length;
        }

        public long available() {
            return length - readIndex;
        }
    }
}
