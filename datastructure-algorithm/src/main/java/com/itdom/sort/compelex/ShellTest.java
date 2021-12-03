package com.itdom.sort.compelex;

import com.itdom.sort.insert.Insert;

import java.util.Arrays;

public class ShellTest {

    public static void sort(Comparable[] arr) {
        int N = arr.length;
        int h = 1;
        while (h < N / 2) {
            h = 2 * h + 1;
        }

        while (h >= 1) {
            for (int i = h; i < N; i++) {
                for (int j = i; j >= h; j -= h) {
                    if (less(arr[j], arr[j-h])) {
                        exchange(arr, j - h, j);
                    } else {
                        break;
                    }
                }
            }
            h /= 2;
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
        Integer[] arr = {6, 7, 3, 2, 4, 1, 5, 9, 8};
        ShellTest.sort(arr);
        System.out.println(Arrays.asList(arr));
    }


}
