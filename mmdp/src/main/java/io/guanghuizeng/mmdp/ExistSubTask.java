package io.guanghuizeng.mmdp;

import io.guanghuizeng.mmdp.rpc.Client;

import java.util.concurrent.Callable;

/**
 * 调用Client, 处理Exist子任务
 */
public class ExistSubTask implements Callable<ExistSubTaskSpec> {

    private Client client;
    private ExistSubTaskSpec subTaskSpec;

    public ExistSubTask(Client client, ExistSubTaskSpec subTaskSpec) {
        this.client = client;
        this.subTaskSpec = subTaskSpec;
    }

    @Override
    public ExistSubTaskSpec call() throws Exception {
        try {
            return client.exist(subTaskSpec);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            throw e;
        }
    }
}
