package io.guanghuizeng.mmdp;

import io.guanghuizeng.fs.Uri;

/**
 * 查找最大项的子任务描述
 */
public class MaxSubTaskSpec {

    private byte opcode = Opcode.MAX;
    private Uri input;
    private Uri output;
    private int count;

    public MaxSubTaskSpec(Uri input, Uri output, int count) {
        this.input = input;
        this.output = output;
        this.count = count;
    }

    public Uri getInput() {
        return input;
    }

    public int getCount() {
        return count;
    }

    public Uri getOutput() {
        return output;
    }

    public void setInput(Uri input) {
        this.input = input;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setOutput(Uri output) {
        this.output = output;
    }

    public MaxSubTaskSpec build(Uri input, Uri output, int count) {
        return new MaxSubTaskSpec(input, output, count);
    }
}
