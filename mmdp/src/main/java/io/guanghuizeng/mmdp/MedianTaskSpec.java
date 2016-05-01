package io.guanghuizeng.mmdp;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by guanghuizeng on 16/4/21.
 */
public class MedianTaskSpec {

    private byte opcode = Opcode.MEDIAN;
    private Path path;
    private Phase phase;
    private long first;
    private long second;
    private long third;
    private long fourth;

    public MedianTaskSpec(byte opcode, Path path, Phase phase) {
        this.opcode = opcode;
        this.path = path;
        this.phase = phase;
    }

    public MedianTaskSpec(byte opcode, Path path, Phase phase, long first) {
        this.opcode = opcode;
        this.path = path;
        this.phase = phase;
        this.first = first;
    }

    public MedianTaskSpec(byte opcode, Path path, Phase phase, long first, long second) {
        this.opcode = opcode;
        this.path = path;
        this.phase = phase;
        this.first = first;
        this.second = second;
    }

    public MedianTaskSpec(byte opcode, Path path, Phase phase, long first, long second, long third) {
        this.opcode = opcode;
        this.path = path;
        this.phase = phase;
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public byte opcode() {
        return opcode;
    }

    public Path path() {
        return path;
    }

    public Phase phase() {
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

    public static MedianTaskSpec build(Path path, Phase phase, long... args) throws IOException {
        switch (phase) {
            case FIRST:
                return new MedianTaskSpec(Opcode.MEDIAN, path, Phase.FIRST);
            case SECOND:
                if (args.length < 1) {
                    throw new IOException("Need argument");
                }
                return new MedianTaskSpec(Opcode.MEDIAN, path, Phase.SECOND, args[0]);
            case THIRD:
                if (args.length < 2) {
                    throw new IOException("Need argument");
                }
                return new MedianTaskSpec(Opcode.MEDIAN, path, Phase.THIRD, args[0], args[1]);
            case FOURTH:
                if (args.length < 3) {
                    throw new IOException("Need argument");
                }
                return new MedianTaskSpec(Opcode.MEDIAN, path, Phase.FOURTH, args[0], args[1], args[2]);
            default:
                throw new IOException("Internal Error.");
        }
    }
}

enum Phase {
    FIRST, SECOND, THIRD, FOURTH
}

