package com.jerry;

import java.io.File;
import java.io.FileReader;

public class jsonHandle {
    private final static String ResourcesFilePath = "src/main/resources/";
    public static String readJSONStringFromFile(final String fileName) {
        FileReader fr;
        File file = new File(ResourcesFilePath + fileName);
        try {
            fr = new FileReader(file);
        } catch(java.io.FileNotFoundException e) {
            System.out.println("Fuck, " + e.getMessage());
            return "";
        }
        char [] temp = new char[9000000];
        try {
            fr.read(temp);
        } catch(java.io.IOException e) {
            System.out.println("Fuck, " + e.getMessage());
            return "";
        }
        String jsonString = new String(temp);
        return jsonString;
    }
}
