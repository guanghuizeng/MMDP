package io.guanghuizeng.mmdp;


import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.fs.VirtualPath;

import java.io.IOException;
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
        // virtual getInput -> uri
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

    public List<MedianSubTaskSpec> map(MedianTaskSpec spec) throws IOException {

        // VP -> Uri
        List<MedianSubTaskSpec> result = new ArrayList<>();
        List<Uri> uris = fileSystem.resolve(spec.getInput());

        for (Uri u : uris) {
            result.add(MedianSubTaskSpec.build(u, spec.phase(), spec.getFirst(),
                    spec.getSecond(), spec.getThird(), spec.getFourth()));
        }

        return result;
    }

    public List<MaxSubTaskSpec> map(MaxTaskSpec spec) {

        List<MaxSubTaskSpec> result = new ArrayList<>();
        List<Uri> uris = fileSystem.resolve(spec.getInput());

        // 生成临时文件
        // List<Uri> tmpUris = new ArrayList<>();

        for (Uri u : uris) {
            Uri tmp = new Uri(u.getServiceID(),
                    new VirtualPath(u.getActualPath().toString()
                            .concat(String.valueOf(System.currentTimeMillis()))));
            // tmpUris.add(tmp);
            result.add(new MaxSubTaskSpec(u, tmp, spec.getCount()));
        }

        return result;
    }

    public List<ExistSubTaskSpec> map(ExistTaskSpec spec) {
        List<ExistSubTaskSpec> result = new ArrayList<>();
        List<Uri> uris = fileSystem.resolve(spec.getInput());
        for (Uri u : uris) {
            result.add(new ExistSubTaskSpec(u, spec.getData(), spec.getFpp()));
        }
        return result;
    }
}
