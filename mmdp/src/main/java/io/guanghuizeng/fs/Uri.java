package io.guanghuizeng.fs;

/**
 * Created by guanghuizeng on 16/4/4.
 */
public class Uri {

    private String prefix = "fs://";
    private String infix = ":";
    private ServiceID serviceID = new ServiceID();
    private VirtualPath actualPath;

    public Uri(VirtualPath path) {
        this.actualPath = path;
    }

    public Uri(String host, int port, VirtualPath Path) {
        serviceID.setHost(host);
        serviceID.setSyncPort(port);
        this.actualPath = Path;
    }

    public Uri(ServiceID serviceID, VirtualPath Path) {
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
