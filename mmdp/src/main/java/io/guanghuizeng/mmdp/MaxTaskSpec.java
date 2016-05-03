package io.guanghuizeng.mmdp;

import io.guanghuizeng.fs.VirtualPath;

/**
 * 查找最大项的任务描述
 */
public class MaxTaskSpec {

    private byte opcode = Opcode.MAX;
    private VirtualPath input;
    private int count;

    public MaxTaskSpec(VirtualPath input, int count) {
        this.input = input;
        this.count = count;
    }

    public VirtualPath getInput() {
        return input;
    }

    public int getCount() {
        return count;
    }

    public void setInput(VirtualPath input) {
        this.input = input;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static MaxTaskSpec build(VirtualPath input, int count) {
        return new MaxTaskSpec(input, count);
    }
}
