package io.guanghuizeng.mmdp;

import io.guanghuizeng.mmdp.rpc.Client;

import java.util.concurrent.Callable;

/**
 * Created by guanghuizeng on 16/5/1.
 */
public class MedianSubTask implements Callable<MedianSubTaskSpec> {

    private Client client;
    private MedianSubTaskSpec subTaskSpec;

    public MedianSubTask(Client client, MedianSubTaskSpec subTaskSpec) {
        this.client = client;
        this.subTaskSpec = subTaskSpec;
    }

    public MedianSubTaskSpec call() {
        try {
            return client.median(subTaskSpec);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
