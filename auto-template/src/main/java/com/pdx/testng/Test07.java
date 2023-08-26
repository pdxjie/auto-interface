package com.pdx.testng;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/*
 * @Author 派同学
 * @Description TODO
 * @Date 2023/8/26
 **/
public class Test07 {
    public static void main(String[] args) throws IOException {
        File file = new File("");
        String cann = file.getCanonicalPath();
        String path = cann + "/test-output";
        System.out.println(path);

        File file1 = new File(path);
        File[] files = file1.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                long diff = o1.lastModified() - o2.lastModified();
                if (diff > 0) {
                    return 1;
                } else if (diff == 0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        System.out.println(files[files.length - 1].getName());
    }
}
