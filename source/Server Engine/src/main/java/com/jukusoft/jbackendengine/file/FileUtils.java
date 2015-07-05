package com.jukusoft.jbackendengine.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Justin on 29.06.2015.
 */
public class FileUtils {

    public static String readFile (String path) throws IOException {
        FileReader reader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(reader);

        StringBuilder sb = new StringBuilder();
        String line = bufferedReader.readLine();

        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = bufferedReader.readLine();
        }

        return sb.toString();
    }

}
