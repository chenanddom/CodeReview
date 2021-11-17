package com.itdom.jdk11;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Opthonal 也增加了几个非常酷的方法，现在可以很方便的将一个 Optional 转换成一个 Stream, 或者当一个空 Optional 时给它一个替代的。
 */
public class OptionalEnhancementTest {
    public static void main(String[] args) {
        String value=null;
        String javastack = Optional.ofNullable(value).orElse("000");
        System.out.println(javastack);

        long count = Optional.of(List.of(1, 2, 3, 4, 5, 6)).stream().count();
        System.out.println(count);

    }
}
