package com.zerses.camelsandbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class NetFileReader {

    public static void main(String[] args) {
        readFile("");

    }

    public static String readFile(String filePath) {
        String theLine = "--  NOTHING READ FROM FILE --";
        try {
            File inFile = new File(filePath); // Using a Windows mapped drive:
                                              // mount
                                              // \\<NFS-IP>\var\nfs\general N:
            if (inFile.exists()) {
                System.out.println("Path exists " + inFile.getAbsolutePath());
            } else {
                System.out.println("Path NOT FOUND " + inFile.getAbsolutePath());
            }
            BufferedReader reader = new BufferedReader(new FileReader(inFile));
            theLine = reader.readLine();
            System.out.println(theLine);
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return theLine;
    }

}
