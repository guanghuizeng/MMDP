package io.guanghuizeng.mmdp;

import io.guanghuizeng.fs.AbsoluteFilePath;
import io.guanghuizeng.fs.ServiceID;
import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.fs.VirtualPath;
import io.guanghuizeng.fs.input.VirtualFile;
import io.guanghuizeng.fs.input.VirtualFileInput;
import io.guanghuizeng.fs.input.VirtualFileInputBuffer;
import io.guanghuizeng.fs.output.VirtualFileOutput;
import io.guanghuizeng.fs.output.WritableVirtualFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by guanghuizeng on 16/4/4.
 */
public class Cluster {

    private FileSystem fileSystem;

    public Cluster(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    /**
     * 数据保存在多台服务器上, 用多个client连接这些服务器
     */
    private HashMap<ServiceID, Client> clients = new HashMap<>();

    public void addNode(String host, int port) throws Exception {
        clients.put(new ServiceID(host, port), (new Client()).start(host, port));
    }

    /**
     * 向文件系统中添加文件
     *
     * @param host 服务器host
     * @param port 服务器port
     * @param path 文件在file system中的路径, 如: "home/data/file.txt"
     */
    public void addFile(String host, int port, VirtualPath path) {
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
        List<AbsoluteFilePath> resolve = fileSystem.resolve(source); // TODO: 检查文件是否存在
        List<AbsoluteFilePath> tmpFiles = new ArrayList<>();
        /**
         * 根据filepath调用相应的client
         */
        for (AbsoluteFilePath path : resolve) {
            String tmpPath = "SortedTmp".concat(String.valueOf(System.currentTimeMillis()));
            AbsoluteFilePath tmp = new AbsoluteFilePath(path.getServiceID()
                    , path.getActualPath().concat(tmpPath));
            tmpFiles.add(tmp);
            fileSystem.put(source.concat(tmpPath), tmp);
            clients.get(path.getServiceID())
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


        // 更换端口 TODO 用更好的方式实现
        List<AbsoluteFilePath> tmpFiles2 = new ArrayList<>();
        List<ServiceID> serverList = fileSystem.getServerList();

        for (int i = 0; i < tmpFiles.size(); i++) {
            if (serverList.size() > i) {
                ServiceID server = serverList.get(i);
                AbsoluteFilePath newPath = new AbsoluteFilePath(server, tmpFiles.get(i).getActualPath());
                tmpFiles2.add(newPath);
            }
        }
        mergeSortedFiles(tmpFiles2, target);
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
    private VirtualPath mergeSortedFiles(final List<AbsoluteFilePath> absoluteFilePaths
            , final VirtualPath target) throws Exception {
        /**
         * initialize a priority queue of VirtualFile
         */
        PriorityQueue<VirtualFileInputBuffer> queue = new PriorityQueue<>();
        for (AbsoluteFilePath path : absoluteFilePaths) {
            VirtualFile file = fileSystem.newFile(path);

            VirtualFileInputBuffer buffer = new VirtualFileInputBuffer((new VirtualFileInput(file)));
            if (!buffer.isEmpty()) {
                queue.add(buffer);
            }
        }

        WritableVirtualFile outputFile = fileSystem.newWritableFile(target);
        VirtualFileOutput fileOutput = new VirtualFileOutput(outputFile);

        try {
            while (queue.size() > 0) {
                VirtualFileInputBuffer buffer = queue.poll();  // 读取
                fileOutput.writeLong(buffer.pop());            // 写入

                if (buffer.isEmpty()) {                        // 判断
                    buffer.close();
                } else {
                    queue.add(buffer);
                }
            }
        } finally {
            // close the connections
            fileOutput.close();
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
