package io.guanghuizeng.mmdp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by guanghuizeng on 16/4/3.
 */
public class Check {

    public static boolean checkReverse(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);

        long last = Long.MAX_VALUE;
        if (ois.available() > 0)
            last = ois.readLong();

        boolean result = true;
        while (ois.available() > 0) {
            long present = ois.readLong();

            result = last >= present;
            if (!result) {
                break;
            }
            last = present;
        }
        ois.close();
        return result;
    }

    public static boolean check(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);

        long last = Long.MIN_VALUE;
        if (ois.available() > 0)
            last = ois.readLong();

        boolean result = true;
        while (ois.available() > 0) {
            long present = ois.readLong();

            result = last <= present;
            if (!result) {
                break;
            }
            last = present;
        }
        ois.close();
        return result;
    }
}
