package com.itdom.guava.smoothcomparator;

import com.google.common.collect.Ordering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SmoothComparatorTest {

public static void test1(){
    Ordering<Comparable> ordering = Ordering.natural();
    Number max = ordering.max(1.0, 1.5);
    Number min = ordering.min(1.0, 1.5);
    System.out.println(max+"--"+min);
    List<Integer> list = Arrays.asList(3, 1, 5, 2, 9, 4, 8, 7);
    System.out.println(ordering.max(list));
    Collections.sort(list,ordering.nullsFirst());
    list.forEach(x->{
        System.out.print(x+"\t");
    });

}

    public static void main(String[] args) {
        SmoothComparatorTest.test1();
    }
}
