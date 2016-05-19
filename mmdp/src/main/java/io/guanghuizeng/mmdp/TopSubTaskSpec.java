package io.guanghuizeng.mmdp;

import io.guanghuizeng.fs.Uri;

import java.util.Map;

/**
 * Created by guanghuizeng on 16/5/4.
 */
public class TopSubTaskSpec {

    private byte opcode = Opcode.TOP;
    private Uri input;
    private long lowBound = Long.MIN_VALUE;
    private long upBound = Long.MAX_VALUE;
    private int k;
    private Map<Long, Long> result;

    public TopSubTaskSpec(Uri input, long lowBound, long upBound, int k, Map<Long, Long> result) {
        this.input = input;
        this.lowBound = lowBound;
        this.upBound = upBound;
        this.k = k;
        this.result = result;
    }

    public Uri getInput() {
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

    public Map<Long, Long> getResult() {
        return result;
    }

    public void setInput(Uri input) {
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

    public void setResult(Map<Long, Long> result) {
        this.result = result;
    }

    public static TopSubTaskSpec build(Uri input, long lowBound, long upBound, int k, Map<Long, Long> result) {
        return new TopSubTaskSpec(input, lowBound, upBound, k, result);
    }

    public static TopSubTaskSpec build(Uri input, long lowBound, long upBound, int k) {
        return new TopSubTaskSpec(input, lowBound, upBound, k, null);
    }
}
