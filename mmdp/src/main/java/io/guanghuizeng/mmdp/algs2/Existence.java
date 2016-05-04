package io.guanghuizeng.mmdp.algs2;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 用 Bloom Filter 判断元素是否存在
 */
public class Existence {

    public static Map<Long, Boolean> mightContain(Path input, Set<Long> data, double fpp) throws IOException {

        // 建立 bloom filter
        File file = new File(input.toUri());
        BloomFilter<Long> filter = BloomFilter.create(Funnels.longFunnel(), file.length(), fpp);

        FileInputBuffer buffer = new FileInputBuffer(input);
        while (!buffer.empty()) {
            filter.put(buffer.pop());
        }
        buffer.close();

        // check
        Map<Long, Boolean> result = new HashMap<>();
        for (long n : data) {
            result.put(n, filter.mightContain(n));
        }

        return result;
    }
}
