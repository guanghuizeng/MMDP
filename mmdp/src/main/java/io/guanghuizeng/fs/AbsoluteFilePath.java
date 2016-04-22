package io.guanghuizeng.fs;

/**
 * Created by guanghuizeng on 16/4/4.
 */
public class AbsoluteFilePath {

    private String prefix = "fs://";
    private String infix = ":";
    private ServiceID serviceID = new ServiceID();
    private VirtualPath actualPath;

    public AbsoluteFilePath(VirtualPath path) {
        this.actualPath = path;
    }

    public AbsoluteFilePath(String host, int port, VirtualPath Path) {
        serviceID.setHost(host);
        serviceID.setSyncPort(port);
        this.actualPath = Path;
    }

    public AbsoluteFilePath(ServiceID serviceID, VirtualPath Path) {
        this.serviceID.copy(serviceID);
        this.actualPath = Path;
    }

    public ServiceID getServiceID() {
        return serviceID;
    }

    public VirtualPath getActualPath() {
        return actualPath;
    }

    public String toString() {
        return prefix.concat(serviceID.getHost())
                .concat(infix)
                .concat(String.valueOf(serviceID.getSyncPort()))
                .concat(actualPath.toString());
    }
}
