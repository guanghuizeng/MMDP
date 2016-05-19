package io.guanghuizeng.mmdp;

import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.fs.ServiceID;
import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.mmdp.rpc.Client;
import io.guanghuizeng.mmdp.algs2.Histogram;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 处理sub tasks, 返回 sub task 结果
 */
public class EngineBackend {

    private FileSystem fileSystem;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private Cluster cluster = new Cluster();

    /**
     * @param fileSystem 计算环境
     */
    public EngineBackend(FileSystem fileSystem) {

        this.fileSystem = fileSystem;

        // file system -> list of service ids
        // cluster <+ client(host, engine port) <- service id
        // 添加 clients
        List<ServiceID> serviceList = fileSystem.getServiceList();
        try {
            for (ServiceID id : serviceList) {
                Client client = new Client().start(id.getHost(), id.getEnginePort());
                cluster.putEngineClient(id, client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param subTaskSpecs
     * @return
     * @throws IOException
     */
    public List<Uri> execute(List<SortSubTaskSpec> subTaskSpecs) throws IOException {

        // sub task specs -> callable sort tasks
        // submit to the executor

        List<Uri> result = new ArrayList<>();
        List<Future<Response>> futures = new ArrayList<>();

        /**
         * 将任务提交到不同线程
         */
        for (SortSubTaskSpec subTask : subTaskSpecs) {
            Client client = cluster.getEngineClient(subTask.getInput().getServiceID());
            futures.add(executor.submit(new SortSubTask(client, subTask)));
            result.add(subTask.getOutput());
        }

        /**
         * 从不同线程获取结果
         */
        try {
            for (Future<Response> f : futures) {
                f.get();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            // TODO 处理异常
            e.printStackTrace();
        }
        return result;
    }

    public List<Histogram> execMedian(List<MedianSubTaskSpec> subTaskSpecs) throws IOException {

        /** 生成task, 放入不同线程 */
        List<Histogram> result = new ArrayList<>();
        List<Future<MedianSubTaskSpec>> futures = new ArrayList<>();

        for (MedianSubTaskSpec subTask : subTaskSpecs) {
            Client client = cluster.getEngineClient(subTask.getInput().getServiceID());
            futures.add(executor.submit(new MedianSubTask(client, subTask)));
        }

        /** 获取结果 */
        try {
            for (Future<MedianSubTaskSpec> f : futures) {
                result.add(f.get().getHistogram());
            }
            return result;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException("EB: execMedian");
        } catch (ExecutionException e) {
            // TODO 改进处理方式
            e.printStackTrace();
            throw new IOException(e.getCause());
        }
    }

    public List<Uri> execMax(List<MaxSubTaskSpec> subTaskSpecs) throws IOException {

        /** 生成tasks, 放入不同线程 */
        List<Uri> result = new ArrayList<>();
        List<Future<MaxSubTaskSpec>> futures = new ArrayList<>();

        for (MaxSubTaskSpec subTask : subTaskSpecs) {
            Client client = cluster.getEngineClient(subTask.getInput().getServiceID());
            futures.add(executor.submit(new MaxSubTask(client, subTask)));
        }

        /** 获取结果 */
        try {
            for (Future<MaxSubTaskSpec> f : futures) {
                result.add(f.get().getOutput());
            }
            return result;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException("EB: execMax");
        } catch (ExecutionException e) {
            // TODO 改进处理方式
            e.printStackTrace();
            throw new IOException(e.getCause());
        }
    }

    public List<Map<Long, Boolean>> execExist(List<ExistSubTaskSpec> subTaskSpecs) throws Exception {

        List<Map<Long, Boolean>> result = new ArrayList<>();
        List<Future<ExistSubTaskSpec>> futures = new ArrayList<>();
        /** 生成tasks, 放入不同线程 */
        for (ExistSubTaskSpec subTask : subTaskSpecs) {
            Client client = cluster.getEngineClient(subTask.getInput().getServiceID());
            futures.add(executor.submit(new ExistSubTask(client, subTask)));
        }
        /** 获取结果 */
        try {
            for (Future<ExistSubTaskSpec> f : futures) {
                result.add(f.get().getResult());
            }
            return result;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException("EB: execExist");
        } catch (ExecutionException e) {
            // TODO 改进处理方式
            e.printStackTrace();
            throw e;
        }
    }

    public List<Map<Long, Long>> execTop2(List<TopSubTaskSpec> subTaskSpecs) throws Exception {
        List<Map<Long, Long>> result = new ArrayList<>();
        EngineBackendExecutor executor = new EngineBackendExecutor();
        for (TopSubTaskSpec subTask : subTaskSpecs) {
            result.add(executor.exec(subTask).getResult());
        }
        return result;
    }

    public List<Map<Long, Long>> execTop(List<TopSubTaskSpec> subTaskSpecs) throws Exception {
        List<Map<Long, Long>> result = new ArrayList<>();
        List<Future<TopSubTaskSpec>> futures = new ArrayList<>();
        /** 生成tasks, 放入不同线程 */
        for (TopSubTaskSpec subTask : subTaskSpecs) {
            Client client = cluster.getEngineClient(subTask.getInput().getServiceID());
            futures.add(executor.submit(new TopSubTask(client, subTask)));
        }
        /** 获取结果 */
        try {
            for (Future<TopSubTaskSpec> f : futures) {
                result.add(f.get().getResult());
            }
            return result;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException("EB: execTop");
        } catch (ExecutionException e) {
            // TODO 改进处理方式
            e.printStackTrace();
            throw e;
        }
    }
}