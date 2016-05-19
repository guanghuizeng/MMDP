package io.guanghuizeng.mmdp;

import io.guanghuizeng.fs.VirtualPath;

/**
 * Created by guanghuizeng on 16/5/4.
 */
public class TopTaskSpec {

    private byte opcode = Opcode.TOP;
    private VirtualPath input;
    private long lowBound = Long.MIN_VALUE;
    private long upBound = Long.MAX_VALUE;
    private int k;

    public TopTaskSpec(VirtualPath input, long lowBound, long upBound, int k) {
        this.input = input;
        this.lowBound = lowBound;
        this.upBound = upBound;
        this.k = k;
    }

    public VirtualPath getInput() {
        return input;
    }

    public long getLowBound() {
        return lowBound;
    }

    public long getUpBound() {
        return upBound;
    }

    public int getK() {
        return k;
    }

    public void setInput(VirtualPath input) {
        this.input = input;
    }

    public void setLowBound(long lowBound) {
        this.lowBound = lowBound;
    }

    public void setUpBound(long upBound) {
        this.upBound = upBound;
    }

    public void setK(int k) {
        this.k = k;
    }

    public static TopTaskSpec build(VirtualPath input, long lowBound, long upBound, int k) {
        return new TopTaskSpec(input, lowBound, upBound, k);
    }
}
