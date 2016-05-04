package io.guanghuizeng.mmdp;

import io.guanghuizeng.fs.Uri;

import java.util.Set;
import java.util.Map;

/**
 * Exist 子任务描述
 */
public class ExistSubTaskSpec {

    private byte opcode = Opcode.EXIST;
    private Uri input;
    private Set<Long> data;
    private double fpp; // false positive percentage
    private Map<Long, Boolean> result;

    public ExistSubTaskSpec(Uri input, Set<Long> data, double fpp) {
        this.input = input;
        this.data = data;
        this.fpp = fpp;
    }

    public ExistSubTaskSpec(Uri input, Set<Long> data, double fpp, Map<Long, Boolean> result) {
        this.input = input;
        this.data = data;
        this.fpp = fpp;
        this.result = result;
    }

    public Uri getInput() {
        return input;
    }

    public Set<Long> getData() {
        return data;
    }

    public double getFpp() {
        return fpp;
    }

    public Map<Long, Boolean> getResult() {
        return result;
    }

    public void setInput(Uri input) {
        this.input = input;
    }

    public void setData(Set<Long> data) {
        this.data = data;
    }

    public void setFpp(double fpp) {
        this.fpp = fpp;
    }

    public void setResult(Map<Long, Boolean> result) {
        this.result = result;
    }

    public static ExistSubTaskSpec build(Uri input, Set<Long> data, double fpp) {
        return new ExistSubTaskSpec(input, data, fpp);
    }

    public static ExistSubTaskSpec build(Uri input, Set<Long> data, double fpp, Map<Long, Boolean> result) {
        return new ExistSubTaskSpec(input, data, fpp, result);
    }

}
