package io.guanghuizeng.fs;

import java.nio.file.Path;

/**
 * Created by guanghuizeng on 16/4/22.
 */
public class VirtualUrl {

    private String prefix = "fs://";
    private String infix = ":";
    private Address address = new Address();
    private Path localPath;

    public VirtualUrl(Address address, Path localPath) {
        this.address.copy(address);
        this.localPath = localPath;
    }

    public Path getLocalPath() {
        return localPath;
    }

    public Address getAddress() {
        return address;
    }

    public String toString() {
        return prefix.concat(address.getHost())
                .concat(infix)
                .concat(String.valueOf(address.getSyncPort()))
                .concat(infix)
                .concat(String.valueOf(address.getEnginePort()))
                .concat(localPath.toString());
    }
}
