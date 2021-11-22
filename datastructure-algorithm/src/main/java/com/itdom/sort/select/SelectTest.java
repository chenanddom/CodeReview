package com.itdom.sort.select;

import java.util.Arrays;

public class SelectTest {
    public static void main(String[] args) {
        Integer[] arr = {2,8,1,3,6,7,4,5};
        Select.sort(arr);
        System.out.println(Arrays.asList(arr));
    }
}
