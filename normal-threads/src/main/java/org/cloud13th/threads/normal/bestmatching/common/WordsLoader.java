package org.cloud13th.threads.normal.bestmatching.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WordsLoader {

    private WordsLoader() {
    }

    public static List<String> load(String path) {
        Path file = Paths.get(path);
        List<String> data = new ArrayList<>();
        try (InputStream in = Files.newInputStream(file); BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }

        return data;
    }
}
