package com.itdom.jdk11;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class InputStreamEnhancementTest {
    public static void main(String[] args) throws IOException {
        var classLoader = ClassLoader.getSystemClassLoader();
        var inputStram = classLoader.getResourceAsStream("javastack.txt");
        var javastack = File.createTempFile("javastack2","txt");
        try(var outputStream = new FileOutputStream(javastack)){
            inputStram.transferTo(outputStream);
        }

    }
}
