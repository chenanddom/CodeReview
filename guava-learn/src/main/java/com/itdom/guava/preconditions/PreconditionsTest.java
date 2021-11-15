package com.itdom.guava.preconditions;

import static com.google.common.base.Preconditions.*;

public class PreconditionsTest {
    public static void test1(int i) {
        checkArgument(i>0,"Parameter cannot be less than %s",i);

    }
    public static void test2(Integer i) {
        Integer value = checkNotNull(i);
        System.out.println(value);
    }

    public static void test3(Integer i){
        checkState(i==null);
    }


    public static void main(String[] args) {
//        PreconditionsTest.test1(0);
//        PreconditionsTest.test2(null);
        PreconditionsTest.test3(null);
    }



}
