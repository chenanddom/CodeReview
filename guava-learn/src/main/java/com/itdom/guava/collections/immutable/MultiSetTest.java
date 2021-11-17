package com.itdom.guava.collections.immutable;

import com.google.common.collect.HashMultiset;

import java.util.Set;

public class MultiSetTest {

    public static void main(String[] args) {
        String str = "this a cat and that is a mice where is the food";
        String[] strings = str.split(" ");
        //将字符串存放到multiset里面
        HashMultiset<String> set = HashMultiset.create();
        for (String temp : strings) {
            set.add(temp);
        }
        //获取multiset里面的元素
        Set<String> elementSet = set.elementSet();
        for (String temp : elementSet) {
            System.out.println(temp+"--------------------->"+set.count(temp));
//            System.out.println(temp);
        }

    }
}
