package com.itdom.jdk11;

import java.util.ArrayList;
import java.util.List;

/**
 * 自 Java 9 开始，Jdk 里面为集合（List/ Set/ Map）都添加了 of 和 copyOf 方法，它们两个都用来创建不可变的集合，来看下它们的使用和区别。
 * 其实是借鉴了guava的特性，只要仔细观察就可以发现是guava特性.
 */
public class CollectionsEnhancementTest {
    public static void main(String[] args) {
        var list1 = List.of("one","two","three");
        var list2 = List.copyOf(list1);
        System.out.println(list1==list2);
        System.out.println("--------------------------------------------");
        var list3 = new ArrayList<String>();
        var list4 = List.copyOf(list3);
        System.out.println(list3==list4);
    }
}
