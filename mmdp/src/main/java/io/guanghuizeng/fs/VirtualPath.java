package io.guanghuizeng.fs;

import java.nio.file.Path;

/**
 * Created by guanghuizeng on 16/4/22.
 */
public class VirtualPath {

    private String prefix = "fs://";
    private Path localPath;

    public VirtualPath(Path localPath) {
        this.localPath = localPath;
    }

    public String toString() {
        return prefix.concat(localPath.toString());
    }

    public Path getLocalPath() {
        return localPath;
    }
}
