package com.bage.my.app.end.util;

import com.bage.my.app.end.entity.M3uEntry;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.core.io.ClassPathResource;

public class M3uParser {
    private static final Pattern EXTINF_PATTERN = Pattern.compile("#EXTINF:-1 tvg-id=\"(.*?)\" tvg-logo=\"(.*?)\" group-title=\"(.*?)\",(.*?)");

    public static List<M3uEntry> parse(String filePath) throws IOException {
        List<M3uEntry> entries = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(filePath);
        StringBuilder allText = new StringBuilder();
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allText.append(line).append("\n");
            }
        }
        String[] lines = allText.toString().split("\n");
        String logo = null;
        String title = null;
        String name = null;
        int id = 1;
        for (int i = 0; i < lines.length; i++) {
            String currentLine = lines[i];
            if (currentLine.startsWith("#EXTINF")) {
                Matcher matcher = EXTINF_PATTERN.matcher(currentLine);
                if (matcher.find()) {
                    logo = matcher.group(2);
                    title = matcher.group(4);
                    name = matcher.group(4);
                }
            } else if (!currentLine.startsWith("#")) {
                String url = currentLine;
                entries.add(new M3uEntry(id++, logo, title, url, name));
                logo = null;
                title = null;
                name = null;
            }
        }
        return entries;
    }
}