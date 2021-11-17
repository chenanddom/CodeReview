package com.itdom.guava.collections.immutable;

import com.google.common.collect.HashBiMap;

/**
 * JDK提供的map可以根据key找到value，而BiMap可以根据value找到key。
 * 在创建biMap的时候内部维护2个map，
 */
public class BiMapTest {
    public static void main(String[] args) {
        HashBiMap<Object, Object> map = HashBiMap.create();
        map.put("k1","v1");
        //根据key获取到value
        System.out.println(map.get("k1"));
        //根据value获取到key
        System.out.println(map.inverse().get("v1"));
    }


}
