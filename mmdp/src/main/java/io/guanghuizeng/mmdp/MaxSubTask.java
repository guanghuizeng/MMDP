package io.guanghuizeng.mmdp;

import io.guanghuizeng.mmdp.rpc.Client;

import java.util.concurrent.Callable;

/**
 * 处理 Max Sub Task.
 */
public class MaxSubTask implements Callable<MaxSubTaskSpec> {

    private Client client;
    private MaxSubTaskSpec subTaskSpec;

    public MaxSubTask(Client client, MaxSubTaskSpec subTaskSpec) {
        this.client = client;
        this.subTaskSpec = subTaskSpec;
    }

    public MaxSubTaskSpec call() {
        try {
            return client.max(subTaskSpec);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
