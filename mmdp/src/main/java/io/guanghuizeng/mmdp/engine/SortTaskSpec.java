package io.guanghuizeng.mmdp.engine;

import io.guanghuizeng.fs.VirtualPath;

/**
 * Created by guanghuizeng on 16/4/22.
 */
public class SortTaskSpec {

    private byte opcode = Opcode.SORT;
    private VirtualPath input;
    private VirtualPath output;

    public SortTaskSpec(VirtualPath input, VirtualPath output) {
        this.input = input;
        this.output = output;
    }

    public VirtualPath getInput() {
        return input;
    }

    public VirtualPath getOutput() {
        return output;
    }

    public static SortTaskSpec build(VirtualPath input, VirtualPath output) {
        return new SortTaskSpec(input, output);
    }
}
