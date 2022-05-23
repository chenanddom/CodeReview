package com.itdom.sort.quick;

public class Quick {
    /**
     * 比较v元素是否小于w元素
     *
     * @param v
     * @param w
     * @return
     */
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static void exchange(Comparable[] a, int i, int j) {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    /*
    对数组元素进行排序
     */
    public static void sort(Comparable[] a) {
        int lo = 0;
        int hi = a.length - 1;
        sort(a, lo, hi);
    }

    /*
    对数组a中从索引lo到索引hi之间的元素进行排序
     */
    private static void sort(Comparable[] a, int lo, int hi) {
        //安全校验
        if (hi <= lo) {
            return;
        }

        //需要对数组中lo索引到hi索引的元素进行分组(左子组和右子组)
        //此处返回的的是分组的分界值所在的索引，分界值的位置变换后的索引
        int partition = partition(a, lo, hi);
        //让左子组有序
        sort(a, lo, partition - 1);
        //让右子组有序
        sort(a, partition + 1, hi);
    }

    /*
    对数组a中的，从索引nlo打拍索引hi之间的元素进行分组，并返回分组界限对于的索引
     */
    public static int partition(Comparable[] a, int lo, int hi) {
        //确定分界值
        Comparable key = a[lo];
        //定义两个指针，一个只想且多为你元素的最小索引处和最大随意出的下一个位置
        int left = lo;
        int right = hi + 1;
        while (true) {
            //从右往左扫描，找到一个比分界值小的元素，停止
            while (less(key, a[--right])) {
                if (right == lo) {
                    break;
                }
            }

            //从左往右扫描，移动left指针，找到一个比分界值大的元素，停止
            while (less(a[++left], key)) {
                if (left == hi) {
                    break;
                }
            }
            //判断left是否大于等于right，则证明扫描完毕，如果不是则进行交换.
            if (left >= right) {
                break;
            } else {
                exchange(a, left, right);
            }
        }
        //交换分界值和最后指针只想的值.
        exchange(a, lo, right);
        return right;
    }
}