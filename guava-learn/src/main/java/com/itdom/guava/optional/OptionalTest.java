package com.itdom.guava.optional;

import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class OptionalTest {

    public static void test1(){
        Integer v1 = null;
        Optional<Integer> integerOptional = Optional.fromNullable(v1);
        if (integerOptional.isPresent()){
            System.out.println(integerOptional.get());
        }else {
            System.out.println("possible is null");
        }
        Integer value = null;
        Optional<Integer> optional = Optional.of(value);
        System.out.println(optional.get());

    }

    public static void test2(){
        Integer v2 = null;
        Optional<Integer> integerOptional = Optional.fromNullable(v2);
        Integer defaultValue = integerOptional.or(100);
        System.out.println(defaultValue);
    }

    public static void test3(){
//        ArrayList<String> strings = new ArrayList<String>();
        ArrayList<String> strings = null;
        Optional<ArrayList<String>> arrayListOptional = Optional.fromNullable(strings);
        System.out.println(arrayListOptional.isPresent());
        Set<ArrayList<String>> arrayLists = arrayListOptional.asSet();
        System.out.println(arrayLists);
    }

    /**
     * 滤掉空元素
     */
    public static void test4(){
        ArrayList<Optional<Integer>> list = new ArrayList<Optional<Integer>>();
        for (int i = 0; i < 10; i++) {
            Integer j = 0==i%2?i:null;
            list.add(Optional.fromNullable(j));
        }
        System.out.println("-------------before-------------------------");
        Iterator<Optional<Integer>> optionalIterator = list.iterator();
        while (optionalIterator.hasNext()){
            Optional<Integer> optional = optionalIterator.next();
            System.out.print(optional.orNull()+"\t");
        }
        System.out.println();
        Iterable<Integer> iterable = Optional.presentInstances(list);
        Iterator<Integer> iterator = iterable.iterator();
        System.out.println("-------------after-------------------------");

        while (iterator.hasNext()){
            System.out.print(iterator.next()+"\t");
        }
    }


    public static void main(String[] args) {
//        OptionalTest.test1();
//        OptionalTest.test2();
//        OptionalTest.test3();
        OptionalTest.test4();
    }
}
