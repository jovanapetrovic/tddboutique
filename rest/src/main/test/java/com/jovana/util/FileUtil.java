package com.jovana.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by jovana on 29.03.2020
 */
public class FileUtil {

    private static final String TEST_RESOURCES_PATH = "src/main/test/resources/";

    public static String readFileAndConvertItToString(String fileName) {
        try {
            String content = Files.readString(Paths.get(TEST_RESOURCES_PATH + fileName));
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
