package com.bage.study.java.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * https://github.com/Snailclimb/JavaGuide/blob/main/docs/java/io/io-basis.md
 */
public class BasicTest {

    public static void main(String[] args) {

        try (FileOutputStream output = new FileOutputStream("output.txt")) {
            byte[] array = "JavaGuide".getBytes();
            output.write(array);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStream fis = new FileInputStream("input.txt")) {
            System.out.println("Number of remaining bytes:" + fis.available());
            int content = -1;
            long skip = fis.skip(2);
            System.out.println("The actual number of bytes skipped:" + skip);
            System.out.print("The content read from file:");
            while ((content = fis.read()) != -1) {
                System.out.print((char) content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}
