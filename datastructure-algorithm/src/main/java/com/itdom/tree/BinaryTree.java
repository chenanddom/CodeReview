package com.itdom.tree;

import com.itdom.linear.Queue;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

/**
 * 1. public void put(Key key,Value value):向树中插入一个键值对
 * 2.private Node put(Node x, Key key, Value val)：给指定树x上，添加键一个键值对，并返回添
 * 加后的新树
 * 3.public Value get(Key key):根据key，从树中找出对应的值
 * 4.private Value get(Node x, Key key):从指定的树x中，找出key对应的值
 * 5.public void delete(Key key):根据key，删除树中对应的键值对
 * 6.private Node delete(Node x, Key key):删除指定树x上的键为key的键值对，并返回删除后的
 * 新树
 * 7.public int size():获取树中元素的个数
 *
 * @param <K>
 * @param <V>
 */
public class BinaryTree<K extends Comparable<K>, V> {
    //根结点
    private Node root;
    //结点个数
    private int N;


    public BinaryTree() {
    }

    public Queue<K> layerErgodic(){
        Queue<K> keys = new Queue<>();
        Queue<Node> nodes = new Queue<>();
         nodes.enqueue(this.root);
         while (!nodes.isEmpty()){
             Node node = nodes.dequeue();
             keys.enqueue(node.k);
             if (node.left!=null){
                 nodes.enqueue(node.left);
             }
             if (node.right!=null){
                 nodes.enqueue(node.right);
             }
         }
         return keys;
    }



    public Queue<K> lastErgodic(){
        Queue<K> keys = new Queue<>();
        lastErgodic(this.root,keys);
        return keys;
    }
    public void lastErgodic(Node node,Queue<K> keys){
        if (node==null){
            return;
        }
        if (node.left!=null){
            lastErgodic(node.left,keys);
        }
        if (node.right!=null){
            lastErgodic(node.right,keys);
        }
        keys.enqueue(node.k);
    }


    public Queue<K> midErgodic() {
        Queue<K> keys = new Queue<>();
        midErgodic(this.root, keys);
        return keys;
    }

    public void midErgodic(Node node, Queue<K> keys) {
        if (node==null){
            return;
        }
        if (node.left!=null){
            midErgodic(node.left,keys);
        }
        keys.enqueue(node.k);
        if (node.right!=null){
            midErgodic(node.right,keys);
        }
    }

    //前序遍历
    public Queue<K> preErgodic() {
        Queue<K> keys = new Queue<>();
        preErgodic(this.root, keys);
        return keys;
    }

    public void preErgodic(Node node, Queue<K> keys) {
        if (node == null) {
            return;
        }
        keys.enqueue(node.k);
        if (node.left != null) {
            preErgodic(node.left, keys);
        }
        if (node.right != null) {
            preErgodic(node.right, keys);
        }
    }

    /**
     * @return
     */
    public K min() {
        return min(root).k;
    }

    public Node min(Node node) {
        if (node.left != null) {
            return min(node.left);
        }
        return node;
    }

    public K max() {
        return max(root).k;
    }

    public Node max(Node node) {
        if (node.right != null) {
            return max(node.right);
        }
        return node;
    }


    public int size() {
        return this.N;
    }

    public void delete(K key) {
        this.root = delete(root, key);
    }

    private Node delete(Node node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.k);
        if (cmp > 0) {
            node.right = delete(node.right, key);
        } else if (cmp < 0) {
            node.left = delete(node.left, key);
        } else {
            this.N--;
            //如果当前结点没有右子树
            if (node.right == null) {
                return node.left;
            }
            //如果当前结点没有左子树
            if (node.left == null) {
                return node.right;
            }
            //如果左右子树都存在
            Node minNode = node.right;
            // 找到右子树中最小的结点
            while (minNode.left != null) {
                minNode = minNode.left;
            }
            //删除（右子树中的）左子树最小的结点
            Node rightNode = node.right;
            while (rightNode.left != null) {
                if (rightNode.left.left == null) {
                    rightNode.left = null;
                } else {
                    rightNode = rightNode.left;
                }
            }
            //让删除结点的左子树称为最小结点的左子树，让删除结点的右子树称为minNode的右子树
            minNode.right = node.right;
            minNode.left = node.left;
            //让被删除结点的父结点指向的最小的结点
            node = minNode;
        }
        return node;
    }


    public V get(K key) {
        return get(root, key);
    }

    private V get(Node node, K key) {
        if (node == null) {
            return null;
        }
        int cmp = key.compareTo(node.k);
        if (cmp > 0) {
            return get(node.right, key);
        } else if (cmp < 0) {
            return get(node.left, key);
        } else {
            return node.v;
        }
    }

    public void put(K key, V value) {
        this.root = put(root, key, value);
    }

    private Node put(Node node, K k, V v) {
        if (node == null) {
            this.N++;
            node = new Node(k, v, null, null);
            return node;
        }
        int cmp = k.compareTo(node.k);
        if (cmp > 0) {
            node.right = put(node.right, k, v);
        } else if (cmp < 0) {
            node.left = put(node.left, k, v);
        } else {
            node.v = v;
        }
        return node;
    }


    private class Node {
        //键
        K k;
        //值
        V v;
        //左子结点
        Node left;
        //右子结点
        Node right;

        public Node(K k, V v, Node left, Node right) {
            this.k = k;
            this.v = v;
            this.left = left;
            this.right = right;
        }
    }

    public static void main(String[] args) {
        BinaryTree<Integer, String> binaryTree = new BinaryTree<>();
        binaryTree.put(4, "zhangsan");
        binaryTree.put(2, "lisi");
        binaryTree.put(3, "wangwu");
        binaryTree.put(1, "zhaoliu");
        binaryTree.put(5, "zhouqi");
        binaryTree.put(6, "wangba");
        //测试前序遍历
        Queue<Integer> preErgodic = binaryTree.preErgodic();
        for (Integer integer : preErgodic) {
            System.out.print(integer + " ");
        }
        System.out.println();
        //测试中序遍历
        Queue<Integer> midQueue = binaryTree.midErgodic();
        for (Integer integer : midQueue) {
            System.out.print(integer+" ");
        }
        System.out.println();
        //测试后续遍历
        Queue<Integer> lastErgodic = binaryTree.lastErgodic();
        for (Integer integer : lastErgodic) {
            System.out.print(integer+" ");
        }
        System.out.println();
        //层遍历
        Queue<Integer> layerErgodic = binaryTree.layerErgodic();
        for (Integer key : layerErgodic) {
            System.out.println(key+"-"+binaryTree.get(key));
        }

        /*System.out.println("最小值:" + binaryTree.min());
        System.out.println("最大值:" + binaryTree.max());

        System.out.println(binaryTree.size());
        System.out.println(binaryTree.get(1));
        binaryTree.delete(3);
        System.out.println(binaryTree.size());
    */
    }
}
