package com.itdom.sort.merge;

import java.util.Arrays;

public class MergeTest {
    public static void main(String[] args) {
        Integer[] a = {8, 4, 5, 7, 1, 3, 6, 2};
        Merge.sort(a);
        System.out.println(Arrays.asList(a));
    }
}
