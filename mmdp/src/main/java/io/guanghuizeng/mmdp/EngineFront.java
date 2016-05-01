package io.guanghuizeng.mmdp;


import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.fs.VirtualPath;

import java.util.ArrayList;
import java.util.List;

/**
 * 负责分解任务, TS -> STS
 */
public class EngineFront {

    private FileSystem fileSystem;

    public EngineFront(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    public List<SortSubTaskSpec> map(SortTaskSpec spec) {
        // map: task -> sub task
        // virtual path -> uri
        List<SortSubTaskSpec> result = new ArrayList<>();
        List<Uri> uris = fileSystem.resolve(spec.getInput());

        //  生成临时文件
        VirtualPath sortedTmp = new VirtualPath(spec.getInput().toString().concat("SortedTmp"));
        List<Uri> tmpUris = new ArrayList<>();

        for (Uri u : uris) {
            Uri tmp = new Uri(u.getServiceID(),
                    new VirtualPath(u.getActualPath().toString()
                            .concat(String.valueOf(System.currentTimeMillis()))));
            tmpUris.add(tmp);
            result.add(new SortSubTaskSpec(u, tmp));
        }
        fileSystem.put(sortedTmp, tmpUris);

        return result;
    }
}
