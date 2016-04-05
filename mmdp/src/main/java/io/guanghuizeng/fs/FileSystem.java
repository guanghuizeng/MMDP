package io.guanghuizeng.fs;

import java.io.File;
import java.util.List;

/**
 * Created by guanghuizeng on 16/4/4.
 */

/**
 * 文件系统
 * <p>
 * 文件系统分为三个层次:
 * <p>
 * 第一层: 文件的相对路径, 如"/data/file.txt"
 * 第二层: 文件的绝对路径, 如"fs://127.0.0.1:8090/mmdpfs/data/file.txt"
 * 第三层: 文件的实际路径, 是文件在Linux机器上的文件路径, 如"/home/users/mmdpfs/data/file.txt"
 * <p>
 * TODO: 要处理这三层路径格式的耦合问题, 如重复出现"/data/file.txt"
 * <p>
 */
public class FileSystem {

    private String HOME;
    private Metadata metadata = new Metadata();

    public FileSystem() {
    }

    public FileSystem(String localHomePath) {
        HOME = localHomePath;
    }

    /**
     * 解析文件
     * <p>
     * 从相对路径解析到绝对路径. 由于一个文件被保存在多台机器上, 一个文件将对应到多个绝对路径
     *
     * @param path
     * @return
     */
    public List<AbsoluteFilePath> resolve(String path) {
        return metadata.resolve(path);
    }

    /**
     * 向metadata中增加文件路径的对应关系
     */
    public void put(String relativePath, AbsoluteFilePath absoluteFilePath) {
        metadata.put(relativePath, absoluteFilePath);
    }

    /**
     * 从一个实际路径中获取实际文件, 被用在server端
     * <p>
     * TODO: 语义不清, 要改进. 用Absolute File Path替代Actual Path.
     */
    public File getFile(String actualPath) {
        String fullPath = HOME.concat(actualPath);
        return new File(fullPath);
    }
}
