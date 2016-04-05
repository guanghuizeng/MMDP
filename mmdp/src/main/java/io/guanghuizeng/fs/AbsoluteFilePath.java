package io.guanghuizeng.fs;

/**
 * Created by guanghuizeng on 16/4/4.
 */
public class AbsoluteFilePath {

    private String prefix = "fs://";
    private String infix = ":";
    private Address address = new Address();
    private String actualPath = "";

    public AbsoluteFilePath(String path) {
        this.actualPath = path;
    }

    public AbsoluteFilePath(String host, int port, String Path) {
        address.setHost(host);
        address.setPort(port);
        this.actualPath = Path;
    }

    public AbsoluteFilePath(Address address, String Path) {
        this.address.copy(address);
        this.actualPath = Path;
    }

    public Address getAddress() {
        return address;
    }

    public String getActualPath() {
        return actualPath;
    }

    public String toString() {
        return prefix.concat(address.getHost())
                .concat(infix)
                .concat(String.valueOf(address.getPort()))
                .concat(actualPath);
    }
}
