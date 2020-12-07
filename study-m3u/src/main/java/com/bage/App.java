package com.bage;

import com.google.gson.Gson;
import net.bjoernpetersen.m3u.M3uParser;
import net.bjoernpetersen.m3u.model.M3uEntry;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {

        // Serialization
        Gson gson = new Gson();

        // Simply pass in a file
        Path m3uFile = Paths.get("E:\\Data\\Chrome Download\\cn.m3u");
        List<M3uEntry> fileEntries = M3uParser.parse(m3uFile);
        System.out.println(gson.toJson(fileEntries));


    }
}
