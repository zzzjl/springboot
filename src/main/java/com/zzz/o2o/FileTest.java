package com.zzz.o2o;

import java.io.File;

public class FileTest {


    public static void main(String[] args) {
        File f = new File("D:/FileTest/try.txt");
        try {
            //f.mkdirs();
            f.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
