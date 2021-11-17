package com.itdom.jdk11;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamEnhancementTest {
    public static void main(String[] args) {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7);
        long count = Stream.ofNullable(list).count();
        System.out.println(count);
        Stream<Integer> list2 = Stream.of(1, 2, 3, 4, 5, 6, 7,null);
        //takeWhile小于某个值时就开始截至了
        List<Integer> integerList = list2.filter(Objects::nonNull).takeWhile(e->e<5).collect(Collectors.toList());
        System.out.println(integerList);
//        System.out.println(list2);
        Stream<Integer> list3 = Stream.of(1, 2, 3, 4, 5, 6, 7,null);
//        这个和上面的相反，一旦 n < 3 不成立就开始计算。
        List<Integer> integers = list3.filter(Objects::nonNull).dropWhile(e -> e<5).collect(Collectors.toList());
        System.out.println(integers);
    }
}
