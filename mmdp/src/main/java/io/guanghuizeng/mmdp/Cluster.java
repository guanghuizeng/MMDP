package io.guanghuizeng.mmdp;

import io.guanghuizeng.fs.AbsoluteFilePath;
import io.guanghuizeng.fs.Address;
import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.fs.VirtualFile;

import java.util.*;

/**
 * Created by guanghuizeng on 16/4/4.
 */
public class Cluster {

    private FileSystem fileSystem = new FileSystem();

    /**
     * 数据保存在多台服务器上, 用多个client连接这些服务器
     */
    private HashMap<Address, Client> clients = new HashMap<>();

    public void addNode(String host, int port) throws Exception {
        clients.put(new Address(host, port), (new Client()).start(host, port));
    }

    /**
     * 向文件系统中添加文件
     *
     * @param host 服务器host
     * @param port 服务器port
     * @param path 文件在file system中的路径, 如: "home/data/file.txt"
     */
    public void addFile(String host, int port, String path) {
        fileSystem.put(path, new AbsoluteFilePath(host, port, path));
    }

    /**
     * 排序: 升序
     * TODO: 增加顺序选项
     *
     * @param source 文件在file system中的路径
     * @param target 文件在file system中的路径
     * @throws Exception
     */
    public void sort(final String source, final String target) throws Exception {
        /**
         * 将source映射到多个absolute filepath,
         * 完成第一层到第二层的映射
         */
        List<AbsoluteFilePath> resolve = fileSystem.resolve(source);

        /**
         * 根据filepath调用相应的client
         */
        for (AbsoluteFilePath path : resolve) {
            String tmpPath = "SortedTmp".concat(String.valueOf(System.currentTimeMillis()));
            AbsoluteFilePath tmp = new AbsoluteFilePath(path.getAddress()
                    , path.getActualPath().concat(tmpPath));
            fileSystem.put(source.concat(tmpPath), tmp);
            clients.get(path.getAddress())
                    .sort(path.getActualPath(), tmp.getActualPath());
        }

        /**
         * TODO: 合并已排序的文件, 按顺序存放到不同机器上
         * 利用FileSystem在不同机器之间读取与写入文件
         *
         * 1. 获取所有部分文件的size
         * 2. 决定好分成几个部分
         * 3. 根据target, 生成部分文件的路径, AbsoluteFilePath
         * 4. 读取tmp文件, 用PriorityQueue, 将最小值写入target
         * 5. 删除tmp文件
         */


    }

    /**
     * merge多个已排序的文件, 并将结果保存到target,
     * TODO: target文件的表示方式需要改进
     * <p>
     * 以ES中的mergeSortedFiles函数为原型
     * <p>
     * 1. 以target为基础, 生成一个抽象的文件
     * 2. 将file paths转换成抽象文件
     * 3. 建立优先队列, 元素类型为抽象文件buffer
     * 4. 不断从PQ中取出元素, 写入target
     *
     * @param absoluteFilePaths
     * @param target
     * @return
     */
    private String mergeSortedFiles(final List<AbsoluteFilePath> absoluteFilePaths, final String target) {
        /**
         * initialize a priority queue of VirtualFile
         */
        PriorityQueue<VirtualFile> queue = new PriorityQueue<>(); // TODO: 重写compareTO
        for (AbsoluteFilePath path : absoluteFilePaths) {
            queue.add(new VirtualFile(path));
        }
        VirtualFile targetFile = new VirtualFile(fileSystem, target);

        while (queue.size() > 0) {
            // 读取
            // 写入
            // 判断
        }

        return target;
    }

    /**
     * 测试
     *
     * @return 一个列表, 包含每台机器的返回结果
     * @throws Exception
     */
    public List<String> echo() throws Exception {
        List<String> response = new ArrayList<>();
        for (Client cli : clients.values()) {
            response.add(cli.echo());
        }
        return response;
    }
}
