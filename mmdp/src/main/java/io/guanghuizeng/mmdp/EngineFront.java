package io.guanghuizeng.mmdp;


import io.guanghuizeng.fs.FileSystem;
import io.guanghuizeng.fs.Uri;
import io.guanghuizeng.fs.VirtualPath;
import io.guanghuizeng.fs.input.VirtualFileInput;
import io.guanghuizeng.fs.input.VirtualFileInputBuffer;
import io.guanghuizeng.fs.output.VirtualFileOutput;

import java.io.IOException;
import java.math.BigInteger;
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

    public List<TopSubTaskSpec> map(TopTaskSpec spec) throws Exception {
        /**
         * 按值域和机器数量, 重新划分文件
         */
        // 设置路径
        List<Uri> uris = fileSystem.resolve(spec.getInput());
        VirtualPath tmp = new VirtualPath(spec.getInput().toString()
                .concat(String.valueOf(System.currentTimeMillis())));
        List<Uri> tmpUris = new ArrayList<>();
        for (Uri u : uris) {
            Uri ut = new Uri(u.getServiceID(), new VirtualPath(u.getActualPath().toString()
                    .concat(String.valueOf(System.currentTimeMillis()))));
            tmpUris.add(ut);
        }
        fileSystem.put(tmp, tmpUris);

        // 读写
        VirtualFileInputBuffer buffer = new VirtualFileInputBuffer(
                new VirtualFileInput(fileSystem.getFile(spec.getInput())));

        List<VirtualFileOutput> outputList = new ArrayList<>();
        for (Uri u : tmpUris) {
            outputList.add(new VirtualFileOutput(fileSystem.newWritableFile(u)));
        }

        // 用BigInteger来避免overflow
        BigInteger low = BigInteger.valueOf(spec.getLowBound());
        BigInteger up = BigInteger.valueOf(spec.getUpBound());
        BigInteger k = BigInteger.valueOf(tmpUris.size());

        try {
            while (!buffer.isEmpty()) {
                BigInteger n = BigInteger.valueOf(buffer.pop());
                // y <= (n-l)/((u-l)/k); x <= (y >= k ? k - 1 : y);  将n映射到合适的index.
                BigInteger x = ((n.subtract(low)).divide((up.subtract(low)).divide(k)));
                if (x.compareTo(k) > 0) {
                    x = k.subtract(BigInteger.ONE);
                }
                outputList.get(x.intValue()).writeLong(n.longValue());
            }
        } finally {
            buffer.close();
            for (VirtualFileOutput output : outputList) {
                output.close();
            }
        }

        // 生成 sub task spec. 关键是要知道每个 sub task 要处理的数值区域(low bound 和 up bound)
        List<TopSubTaskSpec> result = new ArrayList<>();
        BigInteger range = (up.subtract(low)).divide(k);
        BigInteger l = low;
        BigInteger u = l.add(range);
        for (int i = 0; i < k.intValue(); i++) {
            // new spec
            if (u.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
                // overflow
                TopSubTaskSpec subTaskSpec =
                        TopSubTaskSpec.build(tmpUris.get(i), l.longValue(), Long.MAX_VALUE, spec.getK());
                result.add(subTaskSpec);
            } else {
                TopSubTaskSpec subTaskSpec =
                        TopSubTaskSpec.build(tmpUris.get(i), l.longValue(), u.longValue(), spec.getK());
                result.add(subTaskSpec);
            }

            // update
            l = u;
            u = l.add(range);
        }
        return result;
    }
}
