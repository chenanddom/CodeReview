package com.itdom.linear;

import java.util.Iterator;

/**
 * 单项链表
 *1.public void clear()：空置线性表
 * 2.publicboolean isEmpty()：判断线性表是否为空，是返回true，否返回false
 * 3.public int length():获取线性表中元素的个数
 * 4.public T get(int i):读取并返回线性表中的第i个元素的值
 * 5.public void insert(T t)：往线性表中添加一个元素；
 * 6.public void insert(int i,T t)：在线性表的第i个元素之前插入一个值为t的数据元素。
 * 7.public T remove(int i):删除并返回线性表中第i个数据元素。
 * 8.public int indexOf(T t):返回线性表中首次出现的指定的数据元素的位序号，若不存在，则
 * 返回-1。
 */
public class LinkList<T> implements Iterable<T> {
        //头结点
        private Node head;
        //结点个数
        private int N;

    public LinkList() {
        this.head = new Node(null,null);
        this.N=0;
    }

    /*　
        空置线性表
         */
    public void clear(){
    this.N=0;
    }

    /**
     * 判断线性表是否为空，是返回true，否返回false
     * @return
     */
    public boolean isEmpty(){
        return this.N==0;
    }
    /*
    获取线性表中元素的个数
     */
    public int length(){
        return this.N;
    }
    /*
    读取并返回线性表中的第i个元素的值
     */
    public T get(int i){
        if (i<0){
            throw new RuntimeException("Illegal parameter exception");
        }
        Node node = head.next;
        for (int j = 0; j <i ; j++) {
                node = node.next;
        }
        return node.item;
    }

    public void insert(T t){
        Node node = head;
        while (node.next!=null){
            node=node.next;
        }
        Node newNode = new Node(t,null);
        node.next=newNode;
        this.N++;
    }

    /**
     * 在线性表的第i个元素之前插入一个值为t的数据元素。
     * @param i
     * @param t
     */
    public void insert(int i,T t){
        if (i<0||i>N){
            throw new RuntimeException("Illegal parameter exception");
        }
        Node node = head.next;
        for (int j = 0; j <i-1 ; j++) {
            node=node.next;
        }
        Node currentNode = node.next;
        Node newNode = new Node(t,currentNode);
        node.next = newNode;
        this.N++;
    }

    /**
     * 删除并返回线性表中第i个数据元素。
     * @param i
     * @return
     */
    public T remove(int i){
        if (i<0||i>N){
            throw new RuntimeException("Illegal parameter exception");
        }
        Node node = head;
        for (int j = 0; j <i-1 ; j++) {
            node = node.next;
        }
        Node currentNode = node.next;
        node.next = currentNode.next;
        this.N--;
        return currentNode.item;
    }

    /**
     * 返回线性表中首次出现的指定的数据元素的位序号，若不存在，则返回-1。
     * @param t
     * @return
     */
    public int indexOf(T t){
        Node node = head;
        for (int i = 0; node.next!=null; i++) {
                node = node.next;
                if (node.item.equals(t)){
                    return i;
                }
        }
        return -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new CustomerIterator();
    }

    private class CustomerIterator implements Iterator<T>{
        Node currentNode = head;

        @Override
        public boolean hasNext() {
            return currentNode.next!=null;
        }

        @Override
        public T next() {
            currentNode = currentNode.next;
            return currentNode.item;
        }
    }

    private class Node{
        T item;

        Node next;

        public Node(T item, Node next) {
            this.item = item;
            this.next = next;
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

    public void reverse(){
        if (this.N==0){
            return;
        }
        reverse(this.head.next);
    }
    public Node reverse(Node node){
        if (node.next==null){
            this.head.next=node;
            return node;
        }
        Node preNode = reverse(node.next);
        preNode.next=node;
        node.next=null;
        return node;
    }

    public static void main(String[] args) {
        LinkList<String> linkList = new LinkList<>();
        linkList.insert("zhangsan");
        linkList.insert("lisi");
        linkList.insert("wangwu");
        linkList.insert("zhaoliu");
//        System.out.println(linkList.length());
//        System.out.println(linkList.get(2));
//        System.out.println(linkList.indexOf("lisi"));
//        System.out.println(linkList.isEmpty());
//        System.out.println(linkList.remove(0));
//        linkList.clear();
//        System.out.println(linkList.length());
        Iterator<String> iterator = linkList.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        System.out.println("-----------------------");
        linkList.reverse();
        Iterator<String> iterator2 = linkList.iterator();
        while (iterator2.hasNext()){
            System.out.println(iterator2.next());
        }

    }

}
