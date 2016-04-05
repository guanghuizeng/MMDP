package io.guanghuizeng.fs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guanghuizeng on 16/4/5.
 */
public class VirtualFile {

    private List<AbsoluteFilePath> filePaths = new ArrayList<>();

    public VirtualFile(FileSystem fileSystem, String path) {
        /**
         * 从Metadata获取AFP
         */
        this.filePaths = fileSystem.resolve(path);
    }

    public VirtualFile(AbsoluteFilePath absoluteFilePath) {
        filePaths.add(absoluteFilePath);
    }

    /**
     * IO api
     */
    public long readLong() {

        /**
         * 依据AFP, 从本地或remote读取数据
         */

        return 0;
    }
}
