package com.bage.study.java11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilesTest {

    public static void main(String[] args) throws IOException {

        Path path = Paths.get(".", "path-temp.txt");
        // exists
        System.out.println(Files.exists(path));

        // deleteIfExists
        System.out.println(Files.deleteIfExists(path));

        // createFile
        System.out.println(Files.createFile(path));
        System.out.println(Files.exists(path));

        // path
        System.out.println(path.toAbsolutePath().toUri().getPath());

        Path path1 = Files.writeString(path, "hello-world");
        System.out.println("writeString");

        Path path2 = Paths.get(".", "path-temp-cp.txt");
        System.out.println(Files.deleteIfExists(path2));
        Files.copy(path,path2);

        System.out.println(Files.readString(path2));
//        System.out.println(Files.);
//        Files.exists()：检测文件路径是否存在。
//        Files.createFile()：创建文件。
//        Files.createDirectory()：创建文件夹。
//        Files.delete()：删除一个文件或目录。
//        Files.copy()：复制文件。
//        Files.move()：移动文件。
//        Files.size()：查看文件个数。
//        Files.read()：读取文件。
//        Files.write()：写入文件。

    }
}
