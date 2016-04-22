package io.guanghuizeng.fs;

/**
 * Created by guanghuizeng on 16/4/4.
 */
public class AbsoluteFilePath {

    private String prefix = "fs://";
    private String infix = ":";
    private ServiceID serviceID = new ServiceID();
    private String actualPath = "";

    public AbsoluteFilePath(String path) {
        this.actualPath = path;
    }

    public AbsoluteFilePath(String host, int port, String Path) {
        serviceID.setHost(host);
        serviceID.setSyncPort(port);
        this.actualPath = Path;
    }

    public AbsoluteFilePath(ServiceID serviceID, String Path) {
        this.serviceID.copy(serviceID);
        this.actualPath = Path;
    }

    public ServiceID getServiceID() {
        return serviceID;
    }

    public String getActualPath() {
        return actualPath;
    }

    public String toString() {
        return prefix.concat(serviceID.getHost())
                .concat(infix)
                .concat(String.valueOf(serviceID.getSyncPort()))
                .concat(actualPath);
    }
}
