package io.guanghuizeng.mmdp.cluster;

import io.guanghuizeng.fs.sync.SyncServer;
import org.junit.Test;

/**
 * Created by guanghuizeng on 16/4/16.
 */
public class StartSyncServer0 {

    @Test
    public void test0() throws Exception {
        SyncServer syncServer = new SyncServer();
        syncServer.start(8070, "/user1");
    }
}
