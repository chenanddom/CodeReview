package com.itdom.heap;

/*
构造方法：
Heap(int capacity)：创建容量为capacity的Heap对象
成员方法:
1.private boolean less(int i,int j)：判断堆中索引i处的元素是否小于索引j处的元素
2.private void exchange(int i,int j):交换堆中i索引和j索引处的值
3.public T delMax():删除堆中最大的元素,并返回这个最大元素
4.public void insert(T t)：往堆中插入一个元素
5.private void swim(int k):使用上浮算法，使索引k处的元素能在堆中处于一个正确的位置
6.private void sink(int k):使用下沉算法，使索引k处的元素能在堆中处于一个正确的位置
成员变量:
1.private T[] imtes : 用来存储元素的数组
2.private int N：记录堆中元素的个数
 */
public class Heap<T extends Comparable<T>> {
    //存储堆中的元素
    private T[] items;
    //记录堆中的元素个数
    private int N;

    public Heap(int capacity) {
        //因为第一个位置是不存放任何东西的，所以第一个是位置是空出来的
        items = (T[]) new Comparable[capacity + 1];
        this.N = 0;
    }
    /*
    删除堆中最大的元素，并返回这个最大元素
     */
    public T delMax(){
        //最大元素
        T max = items[1];
        exchange(1,N);
        //删除最后位置的元素
        items[N]=null;
        //元素个数-1
        N--;
        sink(1);
        return max;
    }

    /*
     *  判断i处的值是否小于j处的值
     */
    private boolean less(int i, int j) {
        return items[i].compareTo(items[j]) < 0;
    }

    /**
     * :交换堆中i索引和j索引处的值
     */
    private void exchange(int i, int j) {
        T tmp = items[i];
        items[i] = items[j];
        items[j] = tmp;
    }

    /**
     * 往堆中插入一个元素
     *
     * @param t
     */
    public void insert(T t) {
        items[++N] = t;
        sink(N);
    }

    /**
     * 使用上浮算法，使索引k处的元素能在堆中处于一个正确的位置
     * @param k
     */
    private void swim(int k){
        //到了根结点了，就不需要循环了
        while (k>1){
            if (less(k/2,k)){
                exchange(k/2,k);
            }
            k = k/2;
        }
    }
    /**
     * 使用下沉算法，使索引k处的元素能在堆中处于一个正确的位置
     *
     * @param k
     */
    private void sink(int k) {
        //如果当前是最底层了就不需要插入了。
        while (2 * k <= N) {
            //找到子节点教导的结点
            int max;
            if (2 * k + 1 <= N) {
                if (less(2 * k, 2 * k + 1)) {
                    max = 2 * k + 1;
                } else {
                    max = 2 * k;
                }
            } else {
                max = 2 * k;
            }
            //当前结点和较大的子结点比较，如果当前结点不小于就结束循环
            if (!less(k, max)) {
                break;
            }
            //将当前结点和较大的子结点进行交换
            exchange(k, max);
            k = max;
        }
    }

    public static void main(String[] args) {
        Heap<String> heap = new Heap<>(20);
        heap.insert("S");
        heap.insert("G");
        heap.insert("I");
        heap.insert("E");
        heap.insert("N");
        heap.insert("H");
        heap.insert("O");
        heap.insert("A");
        heap.insert("T");
        heap.insert("P");
        heap.insert("R");
        String del;
        while ((del = heap.delMax())!=null){
            System.out.print(del+",");
        }
    }
}
