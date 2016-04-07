package io.guanghuizeng.fs.sync;

import io.guanghuizeng.fs.AbsoluteFilePath;
import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.fs.sync.protocol.SyncMessage;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/*************************
 * 实现文件同步
 *************************/
public class SyncService {

    /**
     * 设置缓存
     */
    private SyncBuffer buffer;

    /**
     * 记录文件总体的length, 是各文件的总和
     */
    private long length = 0;

    /**
     * 设置 sync clients & sync servers
     */
    private List<SyncClient> cluster = new ArrayList<>();
    private List<AbsoluteFilePath> absoluteFilePathList;
    private SyncClient presentClient;

    /*************************
     * Constructors
     *************************/

    public SyncService(FileSystem fileSystem, String path) {

        // 解析 path 到 ABF; 添加 SyncClients 到 cluster
        absoluteFilePathList = fileSystem.resolve(path);
        for (AbsoluteFilePath afp : absoluteFilePathList) {
            cluster.add(new SyncClient(afp));
        }

        // 初始化 length, presentClient, buffer
        for (SyncClient client : cluster) {
            length = length + client.getLength();
        }
        if (!cluster.isEmpty()) {
            presentClient = cluster.get(0);
        }
    }

    public SyncService(FileSystem fileSystem, AbsoluteFilePath afp) {

        // 添加 SyncClient 到 cluster
        cluster.add(new SyncClient(afp));

        // 初始化 length, presentClient, buffer
        length = cluster.get(0).getLength();
        presentClient = cluster.get(0);
    }

    /*************************
     * Sync service
     *************************/

    /**
     * 比较内容是否相同, 然后进行同步
     * 1. 若有不同, 则将buffer保存到对于机器上
     * 2. 若对应机器上没有数据, 则等价于(1).
     * <p>
     * 这么一来, 就与上层设计独立开了! 可以用在读取和写入两种场景中!
     * <p>
     * ps. "同步"的概念, 可以用来写入数据
     * pss. 这里的同步策略是, 将当前buffer中的数据作为最新值
     *
     * @param buffer
     */
    public SyncService sync(final SyncBuffer buffer) {
        /**
         * TODO: 改进. 若是能够记录是否被改动过, 效率会高很多. 重新设计缓存, 可以实现这一点
         */
        if (!this.buffer.equals(buffer)) {
            this.buffer = buffer.copy();        // 更新 buffer
            presentClient.push(buffer.copy());  // 创建一个新的对象, 用来传递数据
            // TODO: 调整 presentClient
        }
        return this;
    }

    /**
     * 更新 buffer: 从 server 获取数据, 写入到buffer
     *
     * @param buffer 以SyncBuffer类型为参数, 且将数据写入到其中,
     *               可以根据buffer的size来决定写入多少数据! 更灵活!
     * @return
     */
    public SyncService next(SyncBuffer buffer) {

        /**
         * - presentClient.pool()
         * - 检查是否要跳到下一个 client
         *
         * 每次push和poll数据后, 可能要调整本地的记录, 如 presentClient
         * buffer 指向一个新的 ByteBuf 对象; 原先的对象将被 GC 处理
         */

        /**
         * 调整 presentClient
         */

        if (presentClient == null) {
            // EOF exception
        }

        // 设置 position, 读取足够多的数据
        this.buffer = presentClient.poll(buffer.getPath(), buffer.getMode(),
                buffer.getPosition() + buffer.capacity(), buffer.getLength());
        // TODO: 调整 presentClient

        buffer.clear();
        buffer.writeBytes(this.buffer); // 将 buffer 中的数据写入 buffer
        return this;
    }


    /**
     * 获取文件大小
     */
    public long getLength() {
        return length;
    }

    public void close() {

    }

    /**
     * TODO: 确保文件不再被使用后, 关闭SyncClients, 释放不同机器上的文件资源
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
