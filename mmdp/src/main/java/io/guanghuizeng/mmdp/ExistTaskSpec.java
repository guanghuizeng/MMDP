package io.guanghuizeng.mmdp;

import io.guanghuizeng.fs.VirtualPath;

import java.util.Set;

/**
 * Exist 任务描述
 */
public class ExistTaskSpec {

    private byte opcode = Opcode.EXIST;
    private VirtualPath input;
    private Set<Long> data;
    private double fpp; // false positive percentage

    public ExistTaskSpec(VirtualPath input, Set<Long> data, double fpp) {
        this.input = input;
        this.data = data;
        this.fpp = fpp;
    }

    public VirtualPath getInput() {
        return input;
    }

    public Set<Long> getData() {
        return data;
    }

    public double getFpp() {
        return fpp;
    }

    public void setInput(VirtualPath input) {
        this.input = input;
    }

    public void setData(Set<Long> data) {
        this.data = data;
    }

    public void setFpp(double fpp) {
        this.fpp = fpp;
    }

    public static ExistTaskSpec build(VirtualPath input, Set<Long> data, double fpp) {
        return new ExistTaskSpec(input, data, fpp);
    }
}
