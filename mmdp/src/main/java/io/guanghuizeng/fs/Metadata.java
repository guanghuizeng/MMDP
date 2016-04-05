package io.guanghuizeng.fs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by guanghuizeng on 16/4/4.
 */

/**
 * 抽象文件路径映射
 * <p>
 * 文件系统分为三个层次:
 * <p>
 * 第一层: 文件的相对路径, 如"/data/file.txt"
 * 第二层: 文件的绝对路径, 如"fs://127.0.0.1:8090/mmdpfs/data/file.txt"
 * 第三层: 文件的实际路径, 是文件在Linux机器上的文件路径, 如"/home/users/mmdpfs/data/file.txt"
 * <p>
 * TODO: 要处理这三层路径格式的耦合问题, 如重复出现"/data/file.txt"
 * <p>
 * 该class的作用是将第一层的文件路径映射到第二层, 依此, 我们可以将一个文件存放到多台机器上
 */
public class Metadata {

    /**
     * key: 第一层文件路径, 用String类型表示;
     * value: 第二层文件路径, 用AbsoluteFilePath类型表示
     * <p>
     */
    private Map<String, List<AbsoluteFilePath>> namespace = new HashMap<>();

    public List<AbsoluteFilePath> resolve(String path) {
        return namespace.get(path);
    }

    /**
     * 将相对路径与绝对路径的对应关系添加到namespace中
     * <p>
     * 路径格式没有设计好, 导致程序难看的呀...
     */
    public void put(String relativePath, AbsoluteFilePath absoluteFilePath) {
        if (namespace.containsKey(relativePath)) {
            namespace.get(relativePath).add(absoluteFilePath);
        } else {
            List<AbsoluteFilePath> paths = new LinkedList<>();
            paths.add(absoluteFilePath);
            namespace.put(relativePath, paths);
        }
    }
}
