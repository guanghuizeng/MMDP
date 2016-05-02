package io.guanghuizeng.mmdp;

import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.mmdp.algs2.Histogram;

/**
 * 表示 median 分解后的子任务
 */
public class MedianSubTaskSpec {

    private byte opcode = Opcode.MEDIAN;
    private Uri input;
    private MedianPhase phase;
    private Histogram histogram;
    private long first;
    private long second;
    private long third;
    private long fourth;

    public MedianSubTaskSpec(byte opcode, Uri input, MedianPhase phase) {
        this.opcode = opcode;
        this.input = input;
        this.phase = phase;
    }

    public MedianSubTaskSpec(byte opcode, Uri input, MedianPhase phase, long first) {
        this.opcode = opcode;
        this.input = input;
        this.phase = phase;
        this.first = first;
    }

    public MedianSubTaskSpec(byte opcode, Uri input, MedianPhase phase, long first, long second) {
        this.opcode = opcode;
        this.input = input;
        this.phase = phase;
        this.first = first;
        this.second = second;
    }

    public MedianSubTaskSpec(byte opcode, Uri input, MedianPhase phase, long first, long second, long third) {
        this.opcode = opcode;
        this.input = input;
        this.phase = phase;
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public byte getOpcode() {
        return opcode;
    }

    public Uri getInput() {
        return input;
    }

    public MedianPhase getPhase() {
        return phase;
    }

    public Histogram getHistogram() {
        return histogram;
    }

    public long getFirst() {
        return first;
    }

    public long getSecond() {
        return second;
    }

    public long getThird() {
        return third;
    }

    public long getFourth() {
        return fourth;
    }

    public void setHistogram(Histogram histogram) {
        this.histogram = histogram;
    }

    public static MedianSubTaskSpec build(Uri input, MedianPhase phase, long... args) {
        switch (phase) {
            case FIRST:
                return new MedianSubTaskSpec(Opcode.MEDIAN, input, MedianPhase.FIRST);
            case SECOND:
                assert args.length >= 1;
                return new MedianSubTaskSpec(Opcode.MEDIAN, input, MedianPhase.SECOND, args[0]);
            case THIRD:
                assert args.length >= 2;
                return new MedianSubTaskSpec(Opcode.MEDIAN, input, MedianPhase.THIRD, args[0], args[1]);
            case FOURTH:
                assert args.length >= 3;
                return new MedianSubTaskSpec(Opcode.MEDIAN, input, MedianPhase.FOURTH, args[0], args[1], args[2]);
            default:
                assert args.length >= 4; // TODO 要更好的解决方式;
                return new MedianSubTaskSpec(Opcode.MEDIAN, input, MedianPhase.FIRST);
        }
    }

    public static MedianSubTaskSpec build(Uri input, Histogram histogram, MedianPhase phase, long... args) {
        MedianSubTaskSpec subTaskSpec;
        switch (phase) {
            case FIRST:
                subTaskSpec = new MedianSubTaskSpec(Opcode.MEDIAN, input, MedianPhase.FIRST);
                subTaskSpec.setHistogram(histogram);
                return subTaskSpec;
            case SECOND:
                assert args.length >= 1;
                subTaskSpec = new MedianSubTaskSpec(Opcode.MEDIAN, input, MedianPhase.SECOND, args[0]);
                subTaskSpec.setHistogram(histogram);
                return subTaskSpec;
            case THIRD:
                assert args.length >= 2;
                subTaskSpec = new MedianSubTaskSpec(Opcode.MEDIAN, input, MedianPhase.THIRD, args[0], args[1]);
                subTaskSpec.setHistogram(histogram);
                return subTaskSpec;
            case FOURTH:
                assert args.length >= 3;
                subTaskSpec = new MedianSubTaskSpec(Opcode.MEDIAN, input, MedianPhase.FOURTH, args[0], args[1], args[2]);
                subTaskSpec.setHistogram(histogram);
                return subTaskSpec;
            default:
                assert args.length >= 4; // TODO 要更好的解决方式;
                subTaskSpec = new MedianSubTaskSpec(Opcode.MEDIAN, input, MedianPhase.FIRST);
                subTaskSpec.setHistogram(histogram);
                return subTaskSpec;
        }
    }
}
