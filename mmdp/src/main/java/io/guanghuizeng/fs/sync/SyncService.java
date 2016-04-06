package io.guanghuizeng.fs.sync;

import io.guanghuizeng.fs.AbsoluteFilePath;
import io.guanghuizeng.fs.FileSystem;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现文件同步
 */
public class SyncService {

    /**
     * 用 ByteBuf 设置缓存
     */
    private int maxCapacity = 1024;
    private ByteBuf cache;

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

    public SyncService(FileSystem fileSystem, String path, int cacheCapacity) {

        // 解析 path 到 ABF; 添加 SyncClients 到 cluster
        absoluteFilePathList = fileSystem.resolve(path);
        for (AbsoluteFilePath afp : absoluteFilePathList) {
            cluster.add(new SyncClient(afp));
        }

        // 初始化 length, presentClient, cache
        for (SyncClient client : cluster) {
            length = length + client.getLength();
        }
        if (!cluster.isEmpty()) {
            presentClient = cluster.get(0);
        }
        maxCapacity = cacheCapacity;
        cache = presentClient.poll(maxCapacity);
    }

    public SyncService(FileSystem fileSystem, AbsoluteFilePath afp, int cacheCapacity) {

        // 添加 SyncClient 到 cluster
        cluster.add(new SyncClient(afp));

        // 初始化 length, presentClient, cache
        length = cluster.get(0).getLength();
        presentClient = cluster.get(0);
        maxCapacity = cacheCapacity;
        cache = presentClient.poll(maxCapacity);
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
    public SyncService sync(final ByteBuf buffer) {
        /**
         * TODO: 改进. 若是能够记录是否被改动过, 效率会高很多.
         */
        if (!cache.equals(buffer)) {
            cache = buffer.copy();  // 更新cache
            push();                 // push cache 到服务器
        }
        return this;
    }

    /**
     * 向 buffer 中写入数据
     *
     * @param buffer 以ByteBuf类型为参数, 且将数据写入到其中,
     *               可以根据buffer的size来决定写入多少数据! 更灵活!
     * @return
     */
    public SyncService next(ByteBuf buffer) {
        poll();                   // 先更新 cache
        buffer.clear();
        buffer.writeBytes(cache); // 将 cache 中的数据写入 buffer
        return this;
    }

    /**
     * 将 cache 发送到 server
     */
    private void push() {
        presentClient.push(cache.copy());  // 创建一个新的对象, 用来传递数据
    }

    /**
     * 更新 cache: 从 server 获取数据, 写入到cache
     */
    private void poll() {
        // cache 指向一个新的 ByteBuf 对象; 原先的对象将被 GC 处理
        cache = presentClient.poll(cache.capacity());
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
