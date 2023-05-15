package fr.hyriode.hyreos.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class IOUtil {

    public static void save(Path path, String content) {
        try {
            Files.createFile(path);
            Files.writeString(path, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String load(Path path) {
        final StringBuilder sb = new StringBuilder();
        if (Files.exists(path)) {
            try (final BufferedReader reader = Files.newBufferedReader(path)) {
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
