package io.guanghuizeng.mmdp;

import io.guanghuizeng.fs.VirtualPath;

import java.io.IOException;


/**
 * Created by guanghuizeng on 16/4/21.
 */
public class MedianTaskSpec {

    private byte opcode = Opcode.MEDIAN;
    private VirtualPath input;
    private MedianPhase phase;
    private long first;
    private long second;
    private long third;
    private long fourth;
    private long length;

    public MedianTaskSpec(VirtualPath input, long length) {
        this.input = input;
        this.length = length;
    }

    public MedianTaskSpec(byte opcode, VirtualPath input, MedianPhase phase) {
        this.opcode = opcode;
        this.input = input;
        this.phase = phase;
    }

    public MedianTaskSpec(byte opcode, VirtualPath input, MedianPhase phase, long first) {
        this.opcode = opcode;
        this.input = input;
        this.phase = phase;
        this.first = first;
    }

    public MedianTaskSpec(byte opcode, VirtualPath input, MedianPhase phase, long first, long second) {
        this.opcode = opcode;
        this.input = input;
        this.phase = phase;
        this.first = first;
        this.second = second;
    }

    public MedianTaskSpec(byte opcode, VirtualPath input, MedianPhase phase, long first, long second, long third) {
        this.opcode = opcode;
        this.input = input;
        this.phase = phase;
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public byte opcode() {
        return opcode;
    }

    public VirtualPath getInput() {
        return input;
    }

    public MedianPhase phase() {
        return phase;
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

    public long getLength() {
        return length;
    }

    public static MedianTaskSpec build(VirtualPath path, MedianPhase phase, long... args) throws IOException {
        switch (phase) {
            case FIRST:
                return new MedianTaskSpec(Opcode.MEDIAN, path, MedianPhase.FIRST);
            case SECOND:
                if (args.length < 1) {
                    throw new IOException("Need argument");
                }
                return new MedianTaskSpec(Opcode.MEDIAN, path, MedianPhase.SECOND, args[0]);
            case THIRD:
                if (args.length < 2) {
                    throw new IOException("Need argument");
                }
                return new MedianTaskSpec(Opcode.MEDIAN, path, MedianPhase.THIRD, args[0], args[1]);
            case FOURTH:
                if (args.length < 3) {
                    throw new IOException("Need argument");
                }
                return new MedianTaskSpec(Opcode.MEDIAN, path, MedianPhase.FOURTH, args[0], args[1], args[2]);
            default:
                throw new IOException("Internal Error.");
        }
    }

    public static MedianTaskSpec build(VirtualPath path, long length) {
        return new MedianTaskSpec(path, length);
    }



}


