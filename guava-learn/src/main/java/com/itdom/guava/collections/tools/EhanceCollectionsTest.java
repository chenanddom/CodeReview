package com.itdom.guava.collections.tools;

import com.google.common.collect.*;
import com.itdom.guava.collections.immutable.ImmutableTest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 增强的集合工具类的实践
 */
public class EhanceCollectionsTest {

    public static void test1() {
        List<Object> list = Lists.newArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.forEach(e -> {
            System.out.println(e);
        });
    }

    public static void test2() {
        LinkedHashMap<Object, Object> map = Maps.newLinkedHashMap();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");
        map.forEach((k, v) -> {
            System.out.println("key:" + k + " value:" + v);
        });
    }

    public static void test3() {
        List<Object> list = Lists.newArrayList();
        list.add("1");
        list.add("2");
        list.add("2");
        list.add("3");
        list.add("3");
        list.add("4");
        //合并多个列表
        Iterable<Object> iterable = Iterables.concat(list);
        Iterator<Object> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            System.out.println(o);
        }
        System.out.println("count:");
        //统计iterables内元素的次数
        System.out.println(Iterables.frequency(iterable, "2"));
        //按数量进行切分，不可更改
        Iterable<List<Object>> lists = Iterables.paddedPartition(list, 2);
        /*
        [1, 2]
        [2, 3]
        [3, 4]
         */
        lists.forEach(e -> System.out.println(e));
        //返回Iterables的第一个元素
        System.out.println(Iterables.getFirst(iterable, 0));
        //返回Iterables的最后一个元素
        System.out.println(Iterables.getLast(iterable, 0));
        //如果两个iterable中的所有元素相等且顺序一致，返回true
        Iterable<Object> iterable2 = Iterables.concat(list);
        System.out.println(Iterables.elementsEqual(iterable, iterable2));
        System.out.println("fluentIterable....");
        FluentIterable<Object> fluentIterable = FluentIterable.concat(list);
        List<Object> distinctList = fluentIterable.stream().distinct().collect(Collectors.toList());
        distinctList.forEach(e->{
            System.out.println(e);
        });
        System.out.println("统计列表的数据...");
        System.out.println(fluentIterable.size());
    }

    public static void test4(){
        List<Integer> list = Lists.newArrayList(3, 1, 2, 5, 4);
        System.out.println(list);
        List<Integer> reverse = Lists.reverse(list);
        System.out.println(reverse);
    }
    /*
    提供了很多标准的集合运算（Set-Theoretic）方法，这些方法接受Set参数并返回SetView，可用于：
    直接当作Set使用，因为SetView也实现了Set接口；
    用copyInto(Set)拷贝进另一个可变集合；
    用immutableCopy()对自己做不可变拷贝。
     */
    public static ImmutableSet<String> test5(){
        ImmutableSet<String> firstSet = ImmutableSet.of("one", "two", "three", "six", "seven", "eight");
        ImmutableSet<String> secondSet = ImmutableSet.of( "two", "three", "five", "seven");

        Sets.SetView<String> intersection = Sets.intersection(firstSet, secondSet);
        System.out.println(intersection);
        System.out.println(Sets.cartesianProduct(firstSet,secondSet));
        return intersection.immutableCopy();

    }

    public static void main(String[] args) {
        EhanceCollectionsTest.test1();
        System.out.println("--------------------------------------");
        EhanceCollectionsTest.test2();
        System.out.println("--------------------------------------");
        EhanceCollectionsTest.test3();
        System.out.println("--------------------------------------");
        EhanceCollectionsTest.test4();
        System.out.println("--------------------------------------");
        ImmutableSet<String> strings = EhanceCollectionsTest.test5();
        System.out.println("result:"+strings);
        System.out.println("--------------------------------------");
    }

}
