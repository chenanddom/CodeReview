package com.itdom.sort.compelex;

import java.util.Arrays;

public class QuickTest {


    public static void sort(Comparable[] arr) {
        int lo = 0;
        int hi = arr.length - 1;
        sort(arr, lo, hi);

    }


    private static void sort(Comparable[] arr, int lo, int hi) {
        if (lo >= hi) {
            return;
        }
        int partition = partition(arr, lo, hi);
        sort(arr, lo, partition - 1);
        sort(arr, partition + 1, hi);
    }


    private static int partition(Comparable[] arr, int lo, int hi) {
        //筛选基准值
        Comparable key = arr[lo];
        int left = lo;
        int right = hi + 1;//数组的后一位还要+1
        while (true) {
            while (less(key, arr[--right])) {
                if (right == lo) {
                    break;
                }
            }
            while (less(arr[++left], key)) {
                if (left == hi) {
                    break;
                }
            }
            if (left>=right){
                break;
            }else {
                exchange(arr, left, right);
            }
        }
        exchange(arr,lo,right);
        return right;
    }


    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }


    private static void exchange(Comparable[] arr, int i, int j) {
        Comparable temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        Integer[] arr = {9,2,1,5,4,8,6,7};
        QuickTest.sort(arr);
        System.out.println(Arrays.asList(arr));
    }
}
