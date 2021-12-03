package com.itdom.sort.merge;

public class Merge {
    //完成归并操作需要辅助数组
    private static Comparable[] assist;

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    /*
    对数组内的元素进行排序
     */
    public static void sort(Comparable[] a) {
        //1.初始化辅助数组assist
        assist = new Comparable[a.length];
        //2. 定义一个lo变量和hi变量，分别记录数组中最小的索引和最大的索引
        int lo = 0;
        int hi = a.length - 1;
        //3. 调用start重载方法完成数组a中从索引lo到索引hi的元素排序
        sort(a, lo, hi);
    }

    /*
    对数组a中从索引lo到索引hi之间的元素进行排序
     */
    private static void sort(Comparable[] a, int lo, int hi) {
        if (lo >= hi) {
            return;
        }
        // 对lo到hi之间的数据进行分为两个组
        int mid = lo + (hi - lo) / 2;
        //分别对每一个数组进行排序
        sort(a, lo, mid);
        sort(a, mid + 1, hi);
        //再把每个数组中的数据进行归并
        merge(a,lo,mid,hi);

    }

    /*
    从索引lo到所以mid为一个子
    组，从索引mid+1到索引hi为另一个子组，把数组a中的这两个子组的数据合并成一个有序的大组（从
    索引lo到索引hi）
     */
    private static void merge(Comparable[] a, int lo, int mid, int hi) {
        //定义三个指针
        int i = lo;
        int p1 = lo;
        int p2 = mid + 1;
        // 遍历，移动p1,p2指针，比较对应的索引的值。找出最小的那个，放到辅助数组对应的索引
        while (p1 <= mid && p2 <= hi) {
            if (less(a[p1], a[p2])) {
                assist[i++] = a[p1++];
            } else {
                assist[i++] = a[p2++];
            }
        }
        //p1指针没有走完就需要移动p1，将数据放到辅助数组的对应位置
        while (p1 <= mid) {
            assist[i++] = a[p1++];
        }
        //p2指针没有走完需要移动，将数组放到辅助数组的对应位置。
        while (p2 <= hi) {
            assist[i++] = a[p2++];
        }
        //把辅助数组中的数据拷贝的原数组。
        for (int j = lo; j <= hi; j++) {
            a[j] = assist[j];
        }
    }

    private static void exchange(Comparable[] arr, int i, int j){
        Comparable temp = 0;
        temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
