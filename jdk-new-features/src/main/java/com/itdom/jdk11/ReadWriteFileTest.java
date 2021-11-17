package com.itdom.jdk11;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 对Files类增加了writeString和readString两个静态方法，可以直接把String写入文件，或者把整个文件读出为一个String：
 */
public class ReadWriteFileTest {
    public static void main(String[] args) throws IOException {
        Files.writeString(Path.of("./","test.txt"),"dasdadadasdasdas", StandardCharsets.UTF_8);
        String readString = Files.readString(Paths.get("./test.txt"), StandardCharsets.UTF_8);
        System.out.println(readString);
    }
}
