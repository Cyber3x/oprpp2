package hr.fer.zemris.java.webserver;

import java.io.File;
import java.nio.file.Path;
import java.util.Random;

public class Utils {

    /**
     * Uppercase letters
     * @param random
     * @param length
     * @return
     */
    public static String generateRandomString(Random random, int length) {
        int leftLimit = 97; // a
        int rightLimit = 122; // z

        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString()
                .toUpperCase();
    }

    public static boolean isPathReadableFile(Path path) {
        File file = path.toFile();
        return file.exists() && file.canRead() && file.isFile();
    }

    /**
     * @param p path
     * @return null if the filename has no extension, otherwise return the extension
     */
    public static String getFileExtension(Path p) {
        String[] parts = p.getFileName().toString().split("\\.");
        if (parts.length != 2) {
            return null;
        }
        return parts[1];
    }

    public record Pair<T, V>(T value1, V value2) {};
}
