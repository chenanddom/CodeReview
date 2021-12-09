package com.itdom.priorityqueue;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.org.apache.regexp.internal.RE;

/**
 * 最大优先队列
 * MaxPriorityQueue(int capacity)：创建容量为capacity的MaxPriorityQueue对象
 * 1.private boolean less(int i,int j)：判断堆中索引i处的元素是否小于索引j处的元素
 * 2.private void exch(int i,int j):交换堆中i索引和j索引处的值
 * 3.public T delMax():删除队列中最大的元素,并返回这个最大元素
 * 4.public void insert(T t)：往队列中插入一个元素
 * 5.private void swim(int k):使用上浮算法，使索引k处的元素能在堆中处于一个正确的位置
 * 6.private void sink(int k):使用下沉算法，使索引k处的元素能在堆中处于一个正确的位置
 * 7.public int size():获取队列中元素的个数
 * 8.public boolean isEmpty():判断队列是否为空
 * 1.private T[] imtes : 用来存储元素的数组
 * 2.private int N：记录堆中元素的个数
 */
public class MaxPriorityQueue<T extends Comparable<T>> {
    //用来啊存储元素的数组
    private T[] items;
    //用来记录堆中元素个数的变量
    private int N;

    public MaxPriorityQueue(int capacity) {
        items = (T[]) new Comparable[capacity + 1];
        N = 0;
    }

    /*
    使用下沉算法，使索引k处的元素能在堆中处于一个正确的位置
     */
    private void sink(int k) {
        while (2 * k <= N) {
        int max = 2 * k;
            if (2 * k + 1 <= N) {
                if (less(2 * k, 2 *k+1)) {
                    max = 2 * k + 1;
                } else {
                    max = 2 * k;
                }
            }
            if (!less(k, max)) {
                break;
            }
            exchange(k, max);
            k = max;
        }
    }


    /*
    删除队列中最大的元素,并返回这个最大元素
     */
    public T delMax() {
        T max = items[1];
        exchange(1, N);
        items[N]=null;//将最后一个元素置空
        N--;
        sink(1);
        return max;
    }


    /**
     * 使用上浮算法，使索引k处的元素能在堆中处于一个正确的位置
     */
    private void swim(int k) {
        while (k > 1) {
            if (less(k / 2, k)) {
                exchange(k / 2, k);
            }
            k = k / 2;
        }
    }


    public void insert(T t) {
        items[++N] = t;
        //由于插入知道导致树结构变乱，需要上浮
        swim(N);
    }

    /**
     * 获取队列中元素的个数
     */
    public int size() {
        return N;
    }

    /**
     * 判断队列是否为空
     */
    public boolean isEmpty() {
        return this.N == 0;
    }

    /**
     * 判断堆中索引i处的元素是否小于索引j处的元素
     */
    private boolean less(int i, int j) {
        return items[i].compareTo(items[j]) < 0;
    }

    /**
     * 交换堆中i索引和j索引处的值
     */
    private void exchange(int i, int j) {
        T tmp = items[i];
        items[i] = items[j];
        items[j] = tmp;
    }

    public static void main(String[] args) {
        MaxPriorityQueue<String> queue = new MaxPriorityQueue<String>(10);
        queue.insert("A");
        queue.insert("B");
        queue.insert("C");
        queue.insert("D");
        queue.insert("E");
        queue.insert("F");
        queue.insert("G");
        queue.insert("H");
        queue.insert("I");

        while (!queue.isEmpty()){
            System.out.println(queue.delMax()+" ");
        }

    }
}
