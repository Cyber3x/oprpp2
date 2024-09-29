package hr.fer.oprpp2.servlets.glasanje;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public static List<GlasanjeEntry> parseGlasanjeEntries(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filename));
        List<GlasanjeEntry> entries = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split("\t");
            String id = parts[0];
            String bandName = parts[1];
            String youtubeLink = parts[2];

            entries.add(new GlasanjeEntry(id, bandName, youtubeLink));
        }

        return entries;
    }

    public static void saveVotes(Map<String, Integer> votes, Path filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        votes.forEach((key, value) -> sb.append(key).append("\t").append(value).append("\n"));
        Files.writeString(filePath, sb.toString());
    }

    public static Map<String, Integer> loadVotes(Path filePath) throws IOException {
        Map<String, Integer> votes = new HashMap<>();

        List<String> rezultatiLines = Files.readAllLines(filePath);
        for (String rezultatiLine : rezultatiLines) {
            if (rezultatiLine.isBlank()) continue;
            String[] parts = rezultatiLine.split("\t");
            votes.put(parts[0], Integer.parseInt(parts[1]));
        }

        return votes;
    }
}
