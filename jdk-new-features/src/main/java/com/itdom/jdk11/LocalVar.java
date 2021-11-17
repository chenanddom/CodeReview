package com.itdom.jdk11;

import java.util.Arrays;
/*
在Lambda表达式中，可以使用var关键字来标识变量，变量类型由编译器自行推断。
局部变量类型推断就是左边的类型直接使用 var 定义，而不用写具体的类型，编译器能根据右边的表达式自动推断类型。
 */
public class LocalVar {
    public static void main(String[] args) {
        var list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        list.forEach((var s)-> System.out.println(s));
    }
}
