package com.itdom.sort.compelex;

import java.util.Arrays;

public class SelectTest {


    public static void sort(Comparable[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (less(arr[j],arr[i])) {
                    exchange(arr, i, j);
                }
            }
        }
    }

    public static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static void exchange(Comparable[] arr, int i, int j) {
        Comparable temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        Integer[] arr = {6,2,1,5,3,9,7,8};
        SelectTest.sort(arr);
        System.out.println(Arrays.asList(arr));

     }

}
