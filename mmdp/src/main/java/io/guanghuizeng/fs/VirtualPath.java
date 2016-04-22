package io.guanghuizeng.fs;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by guanghuizeng on 16/4/22.
 */
public class VirtualPath {

    private Path path;

    public VirtualPath(String first, String... more) {
        path = Paths.get(first, more);
    }

    public String toString() {
        return path.toString();
    }

    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null) return false;
        if (getClass() != that.getClass()) return false;
        if (!(that instanceof VirtualPath)) return false;
        return path.equals(((VirtualPath) that).path);
    }

    public int hashCode() {
        return path.hashCode();
    }
}
