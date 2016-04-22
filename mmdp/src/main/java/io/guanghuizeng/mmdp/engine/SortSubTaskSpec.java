package io.guanghuizeng.mmdp.engine;

import io.guanghuizeng.fs.Uri;

/**
 * Created by guanghuizeng on 16/4/22.
 */
public class SortSubTaskSpec {

    private byte opcode = Opcode.SORT;
    private Uri input;
    private Uri output;

    public SortSubTaskSpec(Uri input, Uri output) {
        this.input = input;
        this.output = output;
    }

    public Uri getInput() {
        return input;
    }

    public Uri getOutput() {
        return output;
    }
}
