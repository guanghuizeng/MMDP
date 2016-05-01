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
        this.serviceID = serviceID;
        this.actualPath = Path;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getInfix() {
        return infix;
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

    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null) return false;
        if (getClass() != that.getClass()) return false;
        if (!(that instanceof Uri)) return false;
        return prefix.equals(((Uri) that).getPrefix()) &&
                infix.equals(((Uri) that).getInfix()) &&
                serviceID.equals(((Uri) that).getServiceID()) &&
                actualPath.equals(((Uri) that).getActualPath());
    }

}
