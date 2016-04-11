package io.guanghuizeng.fs;

import io.guanghuizeng.fs.sync.SyncAttr;
import io.guanghuizeng.fs.sync.SyncBuffer;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
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

    private int bufferSize = 33;
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

    /************************
     * API
     ************************/

    /**
     * next - 生成新的 SyncBuffer, 其中包含SyncInfo, 没有数据. 用SyncService来获取数据.
     */
    public SyncBuffer next() {

        SyncBuffer syncBuffer = new SyncBuffer();
        int endFileIndex = fileList.size();
        int sum = 0;
        int count = 0;

        for (int i = startFileIndex; i < endFileIndex; i++) {
            count++;
            File f = fileList.get(i);
            int pre = sum;
            sum = sum + f.available();
            if (sum >= bufferSize) {
                // 添加到 syncBuffer
                syncBuffer.addComponent(Unpooled.buffer(),
                        new SyncAttr(f.path, f.firstIndex, bufferSize - pre));
                // 更新文件信息
                f.firstIndex = f.firstIndex + (bufferSize - pre);
                break;
            } else {
                // 添加到 syncBuffer
                syncBuffer.addComponent(Unpooled.buffer(),
                        new SyncAttr(f.path, f.firstIndex, f.lastIndex));
                // 更新文件信息
                f.firstIndex = f.lastIndex;
            }
        }
        // 更新文件信息
        startFileIndex = startFileIndex + (count - 1);
        return syncBuffer;
    }

    /************************
     * Others
     ************************/

    public void addFile(AbsoluteFilePath afp, int first, int last) {
        fileList.add(new File(afp, first, last));
        absoluteFilePathList.add(afp);
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

        private int firstIndex;
        private int lastIndex;

        public File(AbsoluteFilePath path, int firstIndex, int lastIndex) {
            this.path = path;
            this.firstIndex = firstIndex;
            this.lastIndex = lastIndex;
        }

        public int available() {
            return lastIndex - firstIndex;
        }
    }
}
