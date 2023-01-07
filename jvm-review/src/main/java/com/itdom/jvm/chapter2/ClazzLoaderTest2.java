package com.itdom.jvm.chapter2;

import sun.misc.Launcher;

import java.net.URL;

public class ClazzLoaderTest2 {
    public static void main(String[] args) {

        System.out.println("--------------启动类加载器--------------------");
        //获取BootstrapClassLoader能够加载的api路径
        URL[] urLs = Launcher.getBootstrapClassPath().getURLs();
        for (URL urL : urLs) {
            System.out.println(urL.toString());
        }
        /**
         * 执行结果:
         * --------------启动类加载器--------------------
         * file:/D:/develop_software/JDK/install_package/jdk-8u311/jre/lib/resources.jar
         * file:/D:/develop_software/JDK/install_package/jdk-8u311/jre/lib/rt.jar
         * file:/D:/develop_software/JDK/install_package/jdk-8u311/jre/lib/sunrsasign.jar
         * file:/D:/develop_software/JDK/install_package/jdk-8u311/jre/lib/jsse.jar
         * file:/D:/develop_software/JDK/install_package/jdk-8u311/jre/lib/jce.jar
         * file:/D:/develop_software/JDK/install_package/jdk-8u311/jre/lib/charsets.jar
         * file:/D:/develop_software/JDK/install_package/jdk-8u311/jre/lib/jfr.jar
         * file:/D:/develop_software/JDK/install_package/jdk-8u311/jre/classes
         */
        System.out.println("--------------------扩展类加载器-------------------------------");
        String extDirs = System.getProperty("java.ext.dirs");
        for (String path : extDirs.split(";")) {
            System.out.println(path);
        }
        /**
         * 执行结果:
         * D:\develop_software\JDK\install_package\jdk-8u311\jre\lib\ext
         * C:\WINDOWS\Sun\Java\lib\ext
         */
    }
}
