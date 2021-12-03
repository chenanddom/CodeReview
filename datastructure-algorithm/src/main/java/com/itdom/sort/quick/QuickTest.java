package com.itdom.sort.quick;

import java.util.Arrays;

public class QuickTest {
    public static void main(String[] args) {
        Integer[] a={6,1,2,7,9,8,3,4,5};
        Quick.sort(a);
        System.out.println(Arrays.asList(a));
    }
}
