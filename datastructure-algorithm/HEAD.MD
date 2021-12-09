[上一节(树)](./TREE.MD)
# 堆
* 定义
```text
堆是计算机科学中一类特殊的数据结构的统称，堆通常可以倍看作是一颗完全二叉树的数组对象
```
* 特点
1. 他是完全二叉树，除了树的最后一层的结点不需要是满的，其他的的每一次从左到右都是满的，如果最后一层结点不是满的，
那么要求左满右不满
2. 她通常用数组来实现
具体的方法就是将二叉树的结点按照层级顺序放入数组中，根结点在位置1，它的结点在位置2和3，而子结点的子结点则拿别在4，5，6和7，以此类推
![二叉树实现的堆](./files\binary_tree_heap_1.PNG)
如果一个系欸但的位置为k，则它的父结点的位置为[k/2]，而它的两个子节点的位置则分别的2k和2k+1。这样，在不使用指针的情况下，我们额可以通过计算数组的
索引在树中上下移动:从a[k]向上一层，就零k等于k/2，向下一层就令k等于2k和2k+1
3. 每个结点都大于等于它的两个子节点，这里需要注意堆中仅仅规定了每个结点大于等于它的两个子节点，但这个子节点的顺序并没有左规定，与我们学习的二叉树是有一定的区别的。

## 堆的实现
### 堆的插入方法的实现
```text
堆是用数组完成的数据元素的存储的，由于数组的底层是一串连续的内存地址，所以我们要往堆中插入树骂我们只能往数组中从索引0处开始，一次往后放数据，
但是堆中对元素的的顺序是有要求的 ，每一个系欸但的疏忽都要大于等于它的两个子结点的数据，所以每次插入一个元素，都会使得堆中的数据顺序变乱，这个
时候需要我们通过一些方法让数据放入到合适的地方。
```

## 堆排序
对构造好得堆，我们只需要左类似于堆得删除操作，就可以完成排序操作。
1. 将堆顶元素和堆中得一个元素交换位置；
2. 通过堆堆顶元素下沉调整堆，把最大的元素放到堆顶(此时最后一个元素不参与堆的调整，因为最大的元素已经到了数组的最右边，相当于是排好了)；
3. 重复1-2的步骤，知道堆中剩下最后一个元素。

```java
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
```

[下一节](./PRIORITY_QUEUE.MD)












