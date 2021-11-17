package com.itdom.guava.collections.immutable;

import com.google.common.collect.ArrayListMultimap;

import java.util.ArrayList;

/**
 * 3.一对多Map:MultiMap[ key可以重复的map ]
 */
public class MultiMapTest {

    public static void main(String[] args) {

        ArrayListMultimap<Object, Object> multimap = ArrayListMultimap.create();
        multimap.put("k1","v1");
        multimap.put("k1","v2");
        multimap.put("k1","v3");
        System.out.println(multimap.get("k1"));

    }

}
