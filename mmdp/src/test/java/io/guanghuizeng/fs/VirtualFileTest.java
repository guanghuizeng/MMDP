package io.guanghuizeng.fs;

import io.guanghuizeng.fs.sync.SyncAttr;
import io.guanghuizeng.fs.input.SyncBuffer;
import io.guanghuizeng.fs.input.VirtualFile;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guanghuizeng on 16/4/10.
 */
public class VirtualFileTest {

    /**
     * 对远程文件的描述
     */
    private class File {
        private AbsoluteFilePath path;

        int first;
        int last;

        public File(int first, int last) {
            this.first = first;
            this.last = last;
        }

        public int available() {
            return last - first;
        }
    }

    /**
     * 多个远程文件
     */
    private List<File> fileList = new ArrayList<>();

    @Test
    public void test1() {

        fileList.add(new File(2, 15));
        fileList.add(new File(0, 9));
        fileList.add(new File(0, 11));
        fileList.add(new File(0, 10));

        List<File> result = new ArrayList<>();
        int expected = 33;

        int pre = 0;
        int acum = 0;
        int lastIndex = 0;
        for (File s : fileList) {
            result.add(s);
            pre = acum;
            acum = acum + s.available();
            lastIndex++;
            if (acum >= expected) {
                break;
            }
        }


        for (int i = 0; i < lastIndex - 1; i++) {
            File s = result.get(i);
            System.out.printf("index = %d, first = %d, last = %d, read = %d\n",
                    i, s.first, s.last, s.available());
        }


        File s = result.get(lastIndex - 1);
        System.out.printf("index = %d, first = %d, last = %d, read = %d\n",
                lastIndex - 1, s.first, (expected - pre), expected - pre);
    }

    @Test
    public void test2() {

        VirtualFile virtualFile = new VirtualFile(16);

        virtualFile.addFile(new AbsoluteFilePath("/user1/f1"), 2, 15);
        virtualFile.addFile(new AbsoluteFilePath("/user1/f2"), 0, 9);
        virtualFile.addFile(new AbsoluteFilePath("/user1/f3"), 0, 11);
        virtualFile.addFile(new AbsoluteFilePath("/user1/f4"), 0, 10);
        virtualFile.addFile(new AbsoluteFilePath("/user1/f5"), 0, 13);
        virtualFile.addFile(new AbsoluteFilePath("/user1/f6"), 0, 11);


        System.out.printf("=== 1 ===\n");
        SyncBuffer b1 = virtualFile.next();
        for (int i = 0; i < b1.numComponents(); i++) {
            SyncAttr a = b1.getSyncAttr(i);
            System.out.printf("path = %s, first = %d, last = %d\n",
                    a.getPath().toString(),a.getPosition(),a.getLength());
        }

        System.out.printf("=== 2 ===\n");
        SyncBuffer b2 = virtualFile.next();
        for (int i = 0; i < b2.numComponents(); i++) {
            SyncAttr a = b2.getSyncAttr(i);
            System.out.printf("path = %s, first = %d, last = %d\n",
                    a.getPath().toString(),a.getPosition(),a.getLength());
        }

        System.out.printf("=== 3 ===\n");
        SyncBuffer b3 = virtualFile.next();
        for (int i = 0; i < b2.numComponents(); i++) {
            SyncAttr a = b3.getSyncAttr(i);
            System.out.printf("path = %s, first = %d, last = %d\n",
                    a.getPath().toString(),a.getPosition(),a.getLength());
        }
    }

}







