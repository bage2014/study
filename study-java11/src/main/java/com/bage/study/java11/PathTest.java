package com.bage.study.java11;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathTest {

    public static void main(String[] args) {
        // https://stackoverflow.com/questions/25167149/missing-scheme-illegalargumentexception-while-using-java-nio-file-paths-interf
//        System.out.println(Path.of(URI.create(""))); Exception Missing scheme

        Path path = Paths.get(".", "Lucas.java");
        System.out.println(path.isAbsolute());

    }

}
