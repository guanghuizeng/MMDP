package io.guanghuizeng.fs;

import io.guanghuizeng.fs.sync.SyncService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * VirtualFile的三个层面:
 * 1. API, 实现业务逻辑, 基于Cache
 * 2. Cache, 数据结构为ByteBuf
 * 3. Sync service, 在Cache与不同机器之间进行同步
 * <p>
 * VirtualFileInput 是只读文件
 */
public class VirtualFileInput {

    private SyncService syncService;

    /**
     * 用 ByteBuf 设置缓存, 用来读取数据
     */
    private int initCapacity = 0;
    private int cacheCapacity = (int) Math.pow(2, 23);     // 8 MB
    private ByteBuf cache = Unpooled.buffer(initCapacity, cacheCapacity);

    private Long availableBytes = 0L; // 文件可读取的数据量
    private long length = 0L;         // 文件大小, 单位是byte

    public VirtualFileInput(FileSystem fileSystem, String path) {
        syncService = new SyncService(fileSystem, path, cacheCapacity);
        length = syncService.getLength(); // 通过sync service, 获取文件大小
        availableBytes = length;
        syncService.next(cache);          // 读入数据到cache中
    }

    public VirtualFileInput(FileSystem fileSystem, AbsoluteFilePath absoluteFilePath) {
        syncService = new SyncService(fileSystem, absoluteFilePath, cacheCapacity);
        length = syncService.getLength(); // 通过sync service, 获取文件大小
        availableBytes = length;
        syncService.next(cache);          // 读入数据到cache中
    }

    /**************
     * API
     **************/
    public long readLong() throws IndexOutOfBoundsException {
        if (availableBytes >= Long.BYTES) {
            long output = cache.readLong();
            availableBytes -= Long.BYTES;              // 减小可读数据量
            if (cache.readableBytes() < 1) {
                syncService.sync(cache).next(cache);
            }
            return output;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * 基于各个部分, 得出总体可读数量
     *
     * @return
     */
    public long available() {
        return availableBytes;
    }

    public void close() {
        // 关闭连接, 将不再可读
        syncService.close();
    }

    /**************
     * Others
     **************/

    /**
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
