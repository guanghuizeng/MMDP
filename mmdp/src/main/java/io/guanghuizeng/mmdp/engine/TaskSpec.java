package io.guanghuizeng.mmdp.engine;


import java.io.IOException;
import java.nio.file.Path;

/**
 * task spec gen
 */
public class TaskSpec {

    public MedianTaskSpec genTaskSpec(Path path, Phase phase, long... args) throws IOException {
        return MedianTaskSpec.build(path, phase, args);
    }

    public SortTaskSpec genTaskSpec() {
        return SortTaskSpec.build();
    }
}
