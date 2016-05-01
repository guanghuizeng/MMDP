package io.guanghuizeng.mmdp;

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

    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null) return false;
        if (getClass() != that.getClass()) return false;
        if (!(that instanceof SortSubTaskSpec)) return false;
        return opcode == ((SortSubTaskSpec) that).opcode &&
                input.equals(((SortSubTaskSpec) that).getInput()) &&
                output.equals(((SortSubTaskSpec) that).getOutput());
    }
}
