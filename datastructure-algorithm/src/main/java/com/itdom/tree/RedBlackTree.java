package com.itdom.tree;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.org.apache.regexp.internal.RE;

import java.util.Random;

/**
 * 成员方法:
 * 1.private boolean isRed(Node x)：判断当前结点的父指向链接是否为红色
 * 2.private Node rotateLeft(Node h):左旋调整
 * 3.private Node rotateRight(Node h):右旋调整
 * 4.private void flipColors(Node h)：颜色反转,相当于完成拆分4-结点
 * 5.public void put(Key key, Value val):在整个树上完成插入操作
 * 6.private Node put(Node h, Key key, Value val):在指定树中，完成插入操作,并返回添加元素后新的树
 * 7.public Value get(Key key):根据key，从树中找出对应的值
 * 8.private Value get(Node x, Key key):从指定的树x中，找出key对应的值
 * 9.public int size():获取树中元素的个数
 * 成员变量:
 * 1.private Node root : 记录根结点
 * 2.private int N:记录树中元素的个数
 * 3.private static final boolean RED：红色链接标识
 * 4.private static final boolean BLACK:黑色链接标识
 */
public class RedBlackTree<K extends Comparable<K>, V> {
    public static void main(String[] args) {
        RedBlackTree<Integer, String> redBlackTree = new RedBlackTree<>();
        redBlackTree.put(4, "二哈4");
        redBlackTree.put(1, "二哈1");
        redBlackTree.put(2, "二哈2");
        redBlackTree.put(5, "二哈5");
        redBlackTree.put(3, "二哈3");
        System.out.println(redBlackTree.size());
        System.out.println(redBlackTree.get(1));
    }

    //记录根结点
    private Node root;
    //记录树中元素的个数
    private int N;
    //海平面黑色链接
    private static final boolean RED = true;
    //黑色链接
    private static final boolean BLACK = false;


    private int size() {
        return this.N;
    }

    /**
     * 从指定的红黑树中根据key查找对应的值
     *
     * @param key
     * @return
     */
    public V get(K key) {
        return get(root, key);
    }

    //从指定的树x中，根据key查找对应的值
    public V get(Node x, K key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return get(x.left, key);
        } else if (cmp > 0) {
            return get(x.right, key);
        } else {
            return x.value;
        }
    }


    /**
     * 在整颗树商完成插入操作
     *
     * @param key   插入键
     * @param value 插入值
     */
    public void put(K key, V value) {
        //在root整个树商插入key-value
        root = put(root, key, value);
        //让根结点的颜色变成BLACK
        root.color = BLACK;
    }

    private Node put(Node h, K key, V value) {
        if (h == null) {
            //标准的插入操作，和父结点用红链接相连
            N++;
            return new Node(key, value, null, null, RED);
        }

        //比较要插入的键和当前结点的键
        int cmp = key.compareTo(h.key);
        if (cmp < 0) {
            //继续寻找左子树插入
            h.left = put(h.left, key, value);
        } else if (cmp > 0) {
            //继续寻找右子树插入
            h.right = put(h.right, key, value);
        } else {
            //已经右相同的结点存在，修改结点的值
            h.value = value;
        }
        //如果当前结点的右链接是红色，左链接是黑色，需要左旋
        if (isRed(h.right) && !isRed(h.left)) {
            h = rotateLeft(h);
        }
        //如果当前结点的左子结点和左子结点的左子结点都是红色链接，则需要右旋转
        if (isRed(h.left) && isRed(h.left.left)) {
            h = rotateRight(h);
        }
        //如果当前结点的左子结点和右子结点都是红色，需要颜色变换
        if (isRed(h.left) && isRed(h.right)) {
            flipColors(h);
        }
        return h;
    }


    /**
     * 左旋转
     *
     * @param h 当前结点，需要找到它的左子结点，左子节点的的右子结点百年变成当前结点的左子结点。
     * @return
     */
    private Node rotateRight(Node h) {
        //找到当前结点的左子结点
        Node hLeft = h.left;
        //找当前的结点的左子结点的右子结点
        Node lhRight = hLeft.right;
        //将当前的结点的左子结点的右子结点设成当前的结点的左子结点。
        h.left = lhRight;
        //将当前结点变成其左子结点的右子结点
        hLeft.right = h;
        //让当前结点h的color变成RED
        h.color = RED;
        //返回当前结点的左子结点
        return hLeft;
    }


    /**
     * 左旋
     *
     * @param h 代表当前结点，需要找到其右结点，还有它的左子结点
     * @return 返回当前结点的右子结点
     */
    private Node rotateLeft(Node h) {
        //找出档期那系欸但的由子结点
        Node hRight = h.right;
        //找出由子结点的左子结点
        Node lhRight = hRight.left;
        //让当前结点的h结点的右子结点的左字节成为当前系欸但的右子结点
        h.right = lhRight;
        //让当前结点成为其右子结点的左子结点
        hRight.left = h;
        //让当前的结点h的color变成RED
        h.color = RED;
        return hRight;
    }

    /**
     * 颜色反转，相当于是万郴和临时4-结点
     *
     * @param h
     */
    private void flipColors(Node h) {
        //简化当前系欸但那的color转换成为RED
        h.color = RED;
        //当前结点的左右子结点的color属性变成黑色
        h.left.color = BLACK;
        h.right.color = BLACK;
    }


    /**
     * 判断当前界定啊的父指向链接是否为红色
     */
    private boolean isRed(Node x) {
        if (x == null) {
            return false;
        }
        return x.color = RED;
    }

    private class Node {
        //村粗键
        public K key;
        //存储值
        public V value;
        //记录左子结点
        public Node left;
        //记录右子结点
        public Node right;
        //由其父结点指向让的链接的颜色。
        public boolean color;

        public Node(K key, V value, Node left, Node right, boolean color) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
            this.color = color;
        }
    }
}
