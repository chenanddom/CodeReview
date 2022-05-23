package com.itdom.sort.shell;

public class Shell {

    public static void sort(Comparable[] arr) {
        int N = arr.length;
        int h = 1;//默认的增量大小
        while (h < N / 2) {
            h = 2 * h + 1;
        }
        while (h > 0) {
            //找到要插入点
            for (int i = h; i < N; i++) {
                for (int j = i; j >= h; j -= h) {
                    if (greater(arr[j - h], arr[j])) {
                        exchange(arr, j - h, j);
                    }
                }
            }
            h /= 2;
        }


    }

    public static boolean greater(Comparable e1, Comparable e2) {
        return e1.compareTo(e2) > 0;
    }

    public static void exchange(Comparable[] arr, int i, int j) {
        Comparable temp = 0;
        temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
