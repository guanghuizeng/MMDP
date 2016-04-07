package io.guanghuizeng.fs.sync;

import io.guanghuizeng.fs.AbsoluteFilePath;

/*************************
 * SyncClient
 * <p>
 * 与 SyncServer 交互
 *************************/
public class SyncClient {

    private SyncClientHandler handler = new SyncClientHandler();

    public SyncClient(AbsoluteFilePath afp) {

    }

    /*****************
     *      API
     *****************/

    /**
     * 将数据保存到 server
     *
     * @param buffer 数据
     */
    public void push(SyncBuffer buffer) {
    }

    /**
     * 从 server 获取数据
     *
     * @param path     路径
     * @param mode     权限
     * @param position 相对位置
     * @param length   要读取的数据量
     * @return 数据
     */
    public SyncBuffer poll(String path, String mode, int position, int length) {
        return null;
    }

    /**
     * 文件大小
     *
     * @return 文件大小
     */
    public long getLength() {
        return 0;
    }
}
