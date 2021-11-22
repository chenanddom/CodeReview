package com.itdom.sort.bubble;

public class Bubble {

    public static void sort(Comparable[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (greater(arr[j], arr[j + 1])) {
                    exchange(arr, j, j + 1);
                }
            }
        }
    }

    /**
     * 元素之间两两比较
     * 次数是判断第一个元素是否大于第二个元素
     *
     * @param e1 元素一
     * @param e2 元素二
     * @return 返回比较结果，>0就是true，<=0就是false
     */
    public static boolean greater(Comparable e1, Comparable e2) {
        return e1.compareTo(e2) > 0;
    }

    /**
     * 前后两两元素比较之后，如果前一个元素大于后面一个元素，那么就会进行元素交换
     *
     * @param arr 数组
     * @param i   前一个元素脚标
     * @param j   后一个元素脚标
     */
    public static void exchange(Comparable[] arr, int i, int j) {
        Comparable temp = 0;
        temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
