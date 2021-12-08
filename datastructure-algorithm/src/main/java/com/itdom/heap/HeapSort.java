package com.itdom.heap;

import java.util.Arrays;

public class HeapSort {
    public static void sort(Comparable[] source) {
        //1. 创建一个比原数组大1的数组
        Comparable[] heap = new Comparable[source.length + 1];
        //2. 构造堆结构
        createHeap(source, heap);
        //3.堆排序
        //3.1 定义一个变量，记录heap中未排序的所有元素中的最大的索引
        int N = heap.length - 1;
        while (N != 1) {
            //3.2将heap中的索引1处的元素和N 的元素交换
            exchange(heap, 1, N);
            N--;
            //3.3对索引1处的元素在0-N范围内做下沉操作
            sink(heap, 1, N);
        }
        //4.heap中的数据已经有序，拷贝到source中
        System.arraycopy(heap, 1, source, 0, source.length);
    }

    private static boolean less(Comparable[] heap, int i, int j) {
        return heap[i].compareTo(heap[j]) < 0;
    }

    private static void exchange(Comparable[] heap, int i, int j) {
        Comparable tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    //
    private static void createHeap(Comparable[] souce, Comparable[] heap) {
        //1.把source中的数据拷贝到heap中，从heap的1索引处开始填充
        System.arraycopy(souce, 0, heap, 1, souce.length);
        //从heap索引的一半处开始倒序遍历，堆得到的每一个元素做下沉操作
        for (int i = (heap.length - 1) / 2; i > 0; i--) {
            sink(heap, i, souce.length - 1);
        }
    }


    //在heap堆中，对target处的元素做下沉，范围是0-range
    private static void sink(Comparable[] heap, int target, int range) {
        //没有子结点了
        while (2 * target <= range) {
            //1. 找出target结点的两个子节点中教导的值
            int max = 2 * target;
            if (2 * target + 1 <= range) {
                if (less(heap, 2 * target, 2 * target + 1)) {
                    max = 2 * target + 1;
                }
            }
            //2. 如果当前结点的值小于结点中的较大的值，则交换
            if (less(heap, target, max)) {
                exchange(heap, target, max);
            }
            target = max;
        }
    }
    public static void main(String[] args) {
        String[] arr = {"S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
        HeapSort.sort(arr);
        System.out.println(Arrays.asList(arr));
    }
}
