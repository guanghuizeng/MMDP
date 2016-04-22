package io.guanghuizeng.mmdp.engine;

import io.guanghuizeng.fs.VirtualPath;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * Created by guanghuizeng on 16/4/22.
 */
public class EngineTest {

    @Test
    public void test() {

        VirtualPath in = new VirtualPath(Paths.get("/data/data1k64"));
        VirtualPath out = new VirtualPath(Paths.get("/data/data1k64Sorted"));

        Engine engine = new Engine();
        engine.sort(in, out);
    }
}
