package io.guanghuizeng.mmdp.engine;

import io.guanghuizeng.fs.VirtualUrl;

/**
 * Created by guanghuizeng on 16/4/22.
 */
public class SortSubTaskSpec {

    private byte opcode = Opcode.SORT;
    private VirtualUrl input;
    private VirtualUrl output;

    public SortSubTaskSpec(VirtualUrl input, VirtualUrl output) {
        this.input = input;
        this.output = output;
    }

    public VirtualUrl getInput() {
        return input;
    }

    public VirtualUrl getOutput() {
        return output;
    }
}
