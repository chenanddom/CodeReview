package com.itdom.priorityqueue;

/**
 * 1.private boolean less(int i,int j)：判断堆中索引i处的元素是否小于索引j处的元素
 * 2.private void exch(int i,int j):交换堆中i索引和j索引处的值
 * 3.public T delMin():删除队列中最小的元素,并返回这个最小元素
 * 4.public void insert(T t)：往队列中插入一个元素
 * 5.private void swim(int k):使用上浮算法，使索引k处的元素能在堆中处于一个正确的位置
 * 6.private void sink(int k):使用下沉算法，使索引k处的元素能在堆中处于一个正确的位置
 * 7.public int size():获取队列中元素的个数
 * 8.public boolean isEmpty():判断队列是否为空
 * 1.private T[] imtes : 用来存储元素的数组
 * 2.private int N：记录堆中元素的个数
 */
public class MinPriorityQueue<T extends Comparable<T>> {
    private T[] items;
    private int N;

    public MinPriorityQueue(int capacity) {
        items = (T[]) new Comparable[capacity + 1];
        N = 0;
    }

    /**
     * 删除队列中最小的元素,并返回这个最小元素
     *
     * @return
     */
    public T delMin() {
        T min = items[1];
        exchange(1, N);
        N--;
        sink(1);
        return min;
    }


    /*
    往队列中插入一个元素
     */
    public void insert(T t) {
        items[++N] = t;
        //插入新的元素需要上浮
        swim(N);

    }

    /*
     *使用上浮算法，使索引k处的元素能在堆中处于一个正确的位置
     */
    private void swim(int k) {
        while (k > 1) {
            //如果当前结点比它的父结点还要小，就和它的父结点进行交换
            if (less(k, k / 2)) {
                exchange(k, k / 2);
            }
            k = k / 2;
        }
    }

    /*
    使用下沉算法，使索引k处的元素能在堆中处于一个正确的位置
     */
    private void sink(int k) {
        while (2 * k <= N) {
            int min = 2 * k;
            if (2 * k + 1 <= N) {
                if (less(2 * k + 1, 2 * k)) {
                    min = 2 * k + 1;
                }
            }
            if (less(k, min)) {
                break;
            }
            exchange(min, k);
            k = min;
        }
    }

    private void exchange(int i, int j) {
        T tmp = items[i];
        items[i] = items[j];
        items[j] = tmp;
    }

    private boolean less(int i, int j) {
        return items[i].compareTo(items[j]) < 0;
    }

    public int size() {
        return N;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public static void main(String[] args) {
        MinPriorityQueue<String> queue = new MinPriorityQueue<>(10);
        queue.insert("A");
        queue.insert("E");
        queue.insert("F");
        queue.insert("G");
        queue.insert("B");
        queue.insert("C");
        queue.insert("D");
        queue.insert("H");
        queue.insert("I");
        while (!queue.isEmpty()) {
            System.out.print(queue.delMin() + " ");
        }
    }
}
