package io.guanghuizeng.fs;

/**
 * 服务ID
 * <p>
 * 包含host, sync port 和 engine port, 分别表示服务器地址, 文件同步服务器端口和数据处理服务器端口.
 */
public class ServiceID {

    private String host = System.getProperty("host", "127.0.0.1");
    private int syncPort = Integer.parseInt(System.getProperty("syncPort", "8070"));
    private int enginePort = Integer.parseInt(System.getProperty("syncPort", "8090"));

    public ServiceID() {
    }

    public ServiceID(String host, int syncPort) {
        this.host = host;
        this.syncPort = syncPort;
    }

    public ServiceID(String host, int syncPort, int enginePort) {
        this.host = host;
        this.syncPort = syncPort;
        this.enginePort = enginePort;
    }

    public ServiceID copy(ServiceID that) {
        this.host = that.host;
        this.syncPort = that.syncPort;
        return this;
    }

    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null) return false;
        if (getClass() != that.getClass()) return false;
        if (!(that instanceof ServiceID)) return false;
        return host.equals(((ServiceID) that).host) && syncPort == ((ServiceID) that).syncPort
                && enginePort == ((ServiceID) that).enginePort;
    }

    public int hashCode() {
        return host.hashCode() + syncPort + enginePort;
    }

    /*********
     * Setter and getter
     ********/

    public void setHost(String host) {
        this.host = host;
    }

    public void setSyncPort(int syncPort) {
        this.syncPort = syncPort;
    }

    public void setEnginePort(int enginePort) {
        this.enginePort = enginePort;
    }

    public String getHost() {
        return host;
    }

    public int getSyncPort() {
        return syncPort;
    }

    public int getEnginePort() {
        return enginePort;
    }
}
