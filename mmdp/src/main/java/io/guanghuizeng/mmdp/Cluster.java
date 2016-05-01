package io.guanghuizeng.mmdp;

import io.guanghuizeng.fs.ServiceID;
import io.guanghuizeng.mmdp.rpc.Client;

import java.util.HashMap;

/**
 * 每个 ServiceID 对应一个 Client
 */
public class Cluster {


    private HashMap<ServiceID, Client> cluster = new HashMap<>();

    public Client getEngineClient(ServiceID id) {
        return cluster.get(id);
    }

    public void putEngineClient(ServiceID id, Client client) {
        cluster.put(id, client);
    }
}
