package com.itdom.guava.collections.immutable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;

import java.util.Collection;

public class ImmutableTest {
private static final ImmutableSet<Color> GOOGLE_COLORS=ImmutableSet.<Color>builder().add(new Color("蓝色",0x112)).build();


public static void test1(Collection<String> collection){
    ImmutableList<String> defensiveCopy = ImmutableList.copyOf(collection);
    System.out.println(defensiveCopy);
    UnmodifiableIterator<String> iterator = defensiveCopy.iterator();
    while (iterator.hasNext()){
        System.out.println(iterator.next());
    }


}


    public static void main(String[] args) {
        ImmutableSet<String> set = ImmutableSet.of("foo", "bar", "baz");
        System.out.println(set);
        ImmutableTest.test1(set);
    }

}
