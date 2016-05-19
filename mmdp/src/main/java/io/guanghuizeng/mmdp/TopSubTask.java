package io.guanghuizeng.mmdp;

import io.guanghuizeng.mmdp.rpc.Client;

import java.util.concurrent.Callable;

/**
 * Created by guanghuizeng on 16/5/6.
 */
public class TopSubTask implements Callable<TopSubTaskSpec> {

    private Client client;
    private TopSubTaskSpec subTaskSpec;

    public TopSubTask(Client client, TopSubTaskSpec subTaskSpec) {
        this.client = client;
        this.subTaskSpec = subTaskSpec;
    }

    @Override
    public TopSubTaskSpec call() throws Exception {
        try {
            return client.top(subTaskSpec);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            return null;
        }

    }
}
