package io.guanghuizeng.fs;

/**
 * Created by guanghuizeng on 16/4/4.
 */

/**
 * 机器地址抽象
 * <p>
 * 包含host和port信息, 用标记机器所在位置和文件的存储位置
 */
public class Address {

    private String host = System.getProperty("host", "127.0.0.1");
    private int port = Integer.parseInt(System.getProperty("port", "8090"));

    public Address() {
    }

    public Address(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Address copy(Address that) {
        this.host = that.host;
        this.port = that.port;
        return this;
    }

    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null) return false;
        if (getClass() != that.getClass()) return false;
        if (!(that instanceof Address)) return false;
        return host.equals(((Address) that).host) && port == ((Address) that).port;
    }

    public int hashCode() {
        return host.hashCode() + port;
    }

    /*********
     * Setter and getter
     ********/

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
