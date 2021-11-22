package com.itdom.sort.insert;

import java.util.Arrays;

public class InsertTest {
    public static void main(String[] args) {
        Integer[] arr = {4,3,2,10,12,1,5,6};
        Insert.sort(arr);
        System.out.println(Arrays.asList(arr));
    }
}
