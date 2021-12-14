package com.itdom.priorityqueue;

/**
 * 索引优先队列.
 * 构造方法:IndexMinPriorityQueue(int capacity)：创建容量为capacity的IndexMinPriorityQueue对象
 * 成员方法:
 * 1.private boolean less(int i,int j)：判断堆中索引i处的元素是否小于索引j处的元素
 * 2.private void exchange(int i,int j):交换堆中i索引和j索引处的值
 * 3.public int delMin():删除队列中最小的元素,并返回该元素关联的索引
 * 4.public void insert(int i,T t)：往队列中插入一个元素,并关联索引i
 * 5.private void swim(int k):使用上浮算法，使索引k处的元素能在堆中处于一个正确的位置
 * 6.private void sink(int k):使用下沉算法，使索引k处的元素能在堆中处于一个正确的位置
 * 7.public int size():获取队列中元素的个数
 * 8.public boolean isEmpty():判断队列是否为空
 * 9.public boolean contains(int k):判断k对应的元素是否存在
 * 10.public void changeItem(int i, T t):把与索引i关联的元素修改为为t
 * 11.public int minIndex():最小元素关联的索引
 * 12.public void delete(int i):删除索引i关联的元素
 * 成员变量:
 * 1.private T[] imtes : 用来存储元素的数组
 * 2.private int[] pq:保存每个元素在items数组中的索引，pq数组需要堆有序
 * 3.private int [] qp:保存qp的逆序，pq的值作为索引，pq的索引作为值
 * 4.private int N：记录堆中元素的个数
 */
public class IndexMinPriorityQueue<T extends Comparable<T>> {
    //用来存储元素的数组
    private T[] items;
    //记录堆中元素的个数
    private int N;
    //保存每个元素在items数组中的索引，pq数组需要堆有序
    private int[] pq;
    // 保存qp的逆序，pq的值作为索引，pq的索引作为值
    private int[] qp;


    public IndexMinPriorityQueue(int capacity) {
        items = (T[]) new Comparable[capacity + 1];
        pq = new int[capacity + 1];
        qp = new int[capacity + 1];
        N = 0;
        for (int i = 0; i < qp.length; i++) {
            //默认情况下qp逆序中不保存任何索引值
            qp[i] = -1;
        }
    }

    public int size() {
        return N;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    //    判断堆中索引i处的元素是否小于索引j处的元素
    private boolean less(int i, int j) {
        return items[pq[i]].compareTo(items[pq[j]]) < 0;
    }

    //    交换堆中i索引和j索引处的值
    private void exchange(int i, int j) {
        int tmp = pq[i];
        pq[i] = pq[j];
        pq[j] = tmp;
        //更新qp数组中的值
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }

    //判断k对应的元素是否存在
    public boolean contains(int k) {
//默认情况下，qp的所有元素都为-1，如果某个位置插入了数据，则不为-1
        return qp[k] != -1;
    }

    //    最小元素关联的索引
    public int minIndex() {
        //pq的索引1处，存放的是最小元素在items中的索引.
        return pq[1];
    }

    //    往队列中插入一个元素,并关联索引i
    public void insert(int i, T t) {
        if (contains(i)) {
        return;
        }
        //增加索引个数
        N++;
        //把元素存放在item是数组中
        items[i] = t;
        //使用pq存放i这个索引
        pq[N] = i;
        //在pq的i索引处存放N
        qp[i] = N;
        swim(N);

    }

    //    删除队列中最小的元素,并返回该元素关联的索引
    public int delMin() {
        //找到items中最小的元素的索引
        int minIndex = pq[1];
        //交换pq中索引1处的值和N处的值
        exchange(1, N);
        //删除qp中索引pq[N]处的值
        qp[pq[N]] = -1;
        // 删除pq索引N处的值
        pq[N] = -1;
        //删除items中最小的元素
        items[minIndex] = null;
        //元素个数-1
        N--;
        //对pq[1]做下沉，让堆有序
        sink(1);
        return minIndex;
    }

    //删除索引i关联的元素
    public void delete(int i) {
        //找出i在pa中的索引
        int k = pq[i];
        //把pq中的索引k处的值和索引N处的值交换
        exchange(k, N);
        //删除qp中索引的pq[N]处的值
        qp[pq[N]] = -1;
        //删除pq中索引N处的值
        pq[N] = -1;
        //删除item是中索引i处的值
        items[i] = null;
        //元素数量-1
        N--;
        sink(k);
        swim(k);

    }

    //把与索引i关联的元素修改为为t
    public void changeItem(int i, T t) {
        //
        items[i] = t;
        int k = pq[i];
        sink(k);
        swim(k);
    }

    //使用上浮算法，使索引k处的元素能在堆中处于一个正确的位置
    private void swim(int k) {
        while (k > 1) {
            if (less(k, k / 2)) {
                exchange(k, k / 2);
            }
            k = k / 2;
        }
    }

    //使用下沉算法，使索引k处的元素能在堆中处于一个正确的位置
    private void sink(int k) {

//如果当前结点已经没有子结点了，则结束下沉
        while (2 * k <= N) {
//找出子结点中的较小值
            int min = 2 * k;
            if (2 * k + 1 <= N && less(2 * k + 1, 2 * k)) {
                min = 2 * k + 1;
            }
//如果当前结点的值比子结点中的较小值小，则结束下沉
            if (less(k, min)) {
                break;
            }
            exchange(k, min);
            k = min;
        }
    }

    public static void main(String[] args) {
        String[] arr = {"S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
        IndexMinPriorityQueue<String> indexMinPQ = new IndexMinPriorityQueue<>(20);
        //插入
        for (int i = 0; i < arr.length; i++) {
            indexMinPQ.insert(i, arr[i]);
        }
        System.out.println(indexMinPQ.size());
        //获取最小值的索引
        System.out.println(indexMinPQ.minIndex());
        //测试修改
        indexMinPQ.changeItem(0, "Z");
        int minIndex = -1;
        while (!indexMinPQ.isEmpty()) {
            minIndex = indexMinPQ.delMin();
            System.out.print(minIndex + ",");
        }
    }
}
