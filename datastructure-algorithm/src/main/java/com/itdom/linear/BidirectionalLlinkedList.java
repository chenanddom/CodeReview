package com.itdom.linear;

import java.util.Iterator;

/**
 * 双向是链表
 * 1.public void clear()：空置线性表
 * 2.publicboolean isEmpty()：判断线性表是否为空，是返回true，否返回false
 * 3.public int length():获取线性表中元素的个数
 * 4.public T get(int i):读取并返回线性表中的第i个元素的值
 * 5.public void insert(T t)：往线性表中添加一个元素；
 * 6.public void insert(int i,T t)：在线性表的第i个元素之前插入一个值为t的数据元素。
 * 7.public T remove(int i):删除并返回线性表中第i个数据元素。
 * 8.public int indexOf(T t):返回线性表中首次出现的指定的数据元素的位序号，若不存在，则
 * 返回-1。
 * 9.public T getFirst():获取第一个元素
 * 10.public T getLast():获取最后一个元素
 */
public class BidirectionalLlinkedList<T> implements Iterable<T> {
    //头结点
    private Node head;
    //尾结点
    private Node last;
    //链表元素的个数
    private int N;

    public BidirectionalLlinkedList() {
        this.head = new Node(null, null, null);
        this.last = null;
        this.N = 0;
    }

    /**
     * 空置线性表
     */
    public void clear() {
        this.N = 0;
        this.last = null;
        this.head.next = this.last;
        this.head.item = null;
    }

    /*
    判断线性表是否为空，是返回true，否返回false
     */
    public boolean isEmpty() {
        return this.N == 0;
    }

    /*
    获取线性表中元素的个数
     */
    public int length() {
        return this.N;
    }

    /*
    读取并返回线性表中的第i个元素的值
     */
    public T get(int i) {
        if (i < 0 || i >= this.N) {
            throw new RuntimeException("Illegal parameter exception");
        }
        Node node = this.head;
        for (int j = 0; j < i; j++) {
            node = node.next;
        }
     return node.next.item;
    }

    /**
     * 往线性表中添加一个元素
     *
     * @param t
     */
    public void insert(T t) {
        if (this.last == null) {
            this.last = new Node(this.head, t, null);
            this.head.next = last;
        } else {
            Node lastNode = this.last;
            Node newNode = new Node(lastNode, t, null);
            lastNode.next = newNode;
            this.last = newNode;
        }
        this.N++;
    }

    /*
    在线性表的第i个元素之前插入一个值为t的数据元素。
     */
    public void insert(int i, T t) {
        if (i < 0 || i >= N) {
            throw new RuntimeException("Illegal parameter exception");
        }
        Node node = this.head;
        for (int index = 0; index < i; index++) {
            node = node.next;
        }
        Node currentNode = node.next;
        Node newNode = new Node(node, t, currentNode);
        currentNode.pre = newNode;
        node.next = newNode;
        this.N++;
    }

    /**
     * 删除并返回线性表中第i个数据元素。
     *
     * @param i
     * @return
     */
    public T remove(int i) {
        if (i < 0 || i >= N) {
            throw new RuntimeException("Illegal parameter exception");
        }
        Node pre = this.head;
        for (int index = 0; index < i; index++) {
            pre = pre.next;
        }
        Node current = pre.next;
        if (i==this.N-1){
            pre.next=null;
            this.last = pre;
        }else {
        Node next = current.next;
        pre.next = next;
        next.pre = pre;

        }
        this.N--;
        return current.item;
    }
    /*
    返回线性表中首次出现的指定的数据元素的位序号，若不存在，则返回-1。
     */
    public int indexOf(T t) {
        Node node = this.head;
        for (int i = 0; i < this.N; i++) {
            node = node.next;
            if (node.item.equals(t)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取第一个元素
     * @return
     */
    public T getFirst(){
        if (isEmpty()){
            return null;
        }
        Node node = head;
        Node first = node.next;
        return first.item;
    }
    public T getLast(){
        if (isEmpty()){
            return null;
        }
        return this.last.item;
    }

    @Override
    public Iterator<T> iterator() {
        return new CustomerIterator();
    }

    private class CustomerIterator implements Iterator<T>{
        Node current = head;

        public CustomerIterator() {

        }

        @Override
        public boolean hasNext() {
            return current.next!=null;
        }

        @Override
        public T next() {
            return current.next.item;
        }
    }


    private class Node {
        Node pre;
        T item;
        Node next;

        public Node(Node pre, T item, Node next) {
            this.pre = pre;
            this.item = item;
            this.next = next;
        }

        public Node getPre() {
            return pre;
        }

        public void setPre(Node pre) {
            this.pre = pre;
        }

        public T getItem() {
            return item;
        }

        public void setItem(T item) {
            this.item = item;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

    public static void main(String[] args) {
        BidirectionalLlinkedList<String> list = new BidirectionalLlinkedList<>();
        list.insert("1");
        list.insert("12");
        list.insert("13");
        list.insert("14");
        list.insert("15");
        System.out.println(list.getFirst());
        System.out.println(list.getLast());
        System.out.println(list.get(3));
        list.remove(4);
        System.out.println(list.getLast());
    }

}
