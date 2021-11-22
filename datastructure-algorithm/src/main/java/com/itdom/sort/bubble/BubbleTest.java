package com.itdom.sort.bubble;

import java.util.Arrays;

public class BubbleTest {
    public static void main(String[] args) {
        Integer[] arr={1,4,2,3,6,5};
       Bubble.sort(arr);
        System.out.println(Arrays.asList(arr));
    }
}
