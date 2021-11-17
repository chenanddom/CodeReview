package com.itdom.jdk11;

/**
 * String新增了strip()方法，和trim()相比，strip()可以去掉Unicode空格，例如，中文空格：
 */
public class StringEnhancement {
    public static void main(String[] args) {
        //判断字符串是否为空白
        System.out.println("x".isBlank());
        //去除首位空格
        System.out.println(" Java Test ".strip());
        //去除尾部空格
        System.out.println(" Java Test ".stripTrailing());
        //去除头部的空格
        System.out.println(" Java Test ".stripLeading());
        //复制字符串
        System.out.println(" Java Test ".repeat(3));
        //统计行数
        System.out.println("1\r\n2\r\n3\r\n".lines().count());

    }

}
