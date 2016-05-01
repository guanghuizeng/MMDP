package io.guanghuizeng.mmdp;

import io.guanghuizeng.mmdp.rpc.Client;

import java.util.concurrent.Callable;

/**
 * Created by guanghuizeng on 16/4/30.
 */
public class SortSubTask implements Callable<Response> {
    private Client client;
    private SortSubTaskSpec spec;

    public SortSubTask(Client client, SortSubTaskSpec spec) {
        this.client = client;
        this.spec = spec;
    }

    public Response call() {
        try {
            client.sort(spec);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return new Response(); // TODO 需要返回什么结果?
    }
}