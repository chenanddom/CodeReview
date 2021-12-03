package com.itdom.sort.compelex;

import java.util.Arrays;

public class MergeTest {

    private static Comparable[] assist;

    public static void sort(Comparable[] arr) {
        assist = new Comparable[arr.length];
        int lo = 0;
        int hi = arr.length - 1;
        sort(arr, lo, hi);
    }


    private static void sort(Comparable[] arr, int lo, int hi) {
        if (lo >= hi) {
            return;
        }
        int mid = lo + (hi - lo) / 2;
        sort(arr, lo, mid);
        sort(arr, mid + 1, hi);
        merge(arr, lo, mid, hi);
    }

    private static void merge(Comparable[] arr, int lo, int mid, int hi) {
        int i = lo;
        int p1 = lo;
        int p2 = mid + 1;
        while (p1 <= mid && p2 <= hi) {
            assist[i++] = less(arr[p1], arr[p2]) ? arr[p1++] : arr[p2++];
        }
        while (p1 <= mid) {
            assist[i++] = arr[p1++];
        }
        while (p2 <= hi) {
            assist[i++] = arr[p2++];
        }
        //到目前位置assist数组内的数据从lo到hi的数据都是有序的，秩序将assist内的数据拷贝到arr数组即可
        for (int index = lo;index<=hi;index++){
            arr[index]=assist[index];
        }
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
        Integer[] arr = {6,1,4,3,2,7,9,8,5};
        MergeTest.sort(arr);
        System.out.println(Arrays.asList(arr));
    }
}
