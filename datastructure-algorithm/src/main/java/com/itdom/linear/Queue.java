package com.itdom.linear;

import java.util.Iterator;

/**
 * 1.public boolean isEmpty()：判断队列是否为空，是返回true，否返回false
 * 2.public int size():获取队列中元素的个数
 * 3.public T dequeue():从队列中拿出一个元素
 * 4.public void enqueue(T t)：往队列中插入一个元素
 *
 * 1.private Node head:记录首结点
 * 2.private int N:当前栈的元素个数
 * 3.private Node last:记录最后一个结点
 */
public class Queue<T> implements Iterable<T> {
    //定义头结点
    private Node head;
    //结点个数
    private int N;
    //定义尾结点
    private Node last;

    public Queue() {
        this.head = new Node(null,null);
        this.last = null;
        this.N=0;
    }
    public boolean isEmpty(){
        return this.N==0;
    }
    public int size(){
        return this.N;
    }

    public void enqueue(T t){
        this.N++;
        if (this.last==null){
            Node newNode = new Node(t,null);
            head.next = newNode;
            this.last=newNode;
            return;
        }
        Node curr = this.last;
        Node newNode = new Node(t,null);
        curr.next = newNode;
        this.last = newNode;
    }

    public T dequeue(){
        if (isEmpty()){
            return null;
        }
        Node current = this.head.next;
        this.head.next = current.next;
        this.N--;
        if (isEmpty()) {
        this.last=null;
        }
        return (T) current.item;
    }

    @Override
    public Iterator<T> iterator() {
        return new CustomerIterater();
    }
    public class CustomerIterater implements Iterator<T>{

        Node  h=head;

        @Override
        public boolean hasNext() {
            return h.next!=null;
        }

        @Override
        public T next() {
            Node current = h.next;
            h=h.next;
        return (T) current.item;
        }
    }

    private static class Node<T>{
        T item;
        private Node next;

        public Node(T item, Node next) {
            this.item = item;
            this.next = next;
        }
    }

    public static void main(String[] args) {
        Queue<String> queue = new Queue<>();
        queue.enqueue("1");
        queue.enqueue("2");
        queue.enqueue("3");
        queue.enqueue("4");
        queue.enqueue("5");
        Iterator<String> iterator = queue.iterator();
        while (iterator.hasNext()){
            System.out.print(iterator.next()+",");
        }
        System.out.println();
        System.out.println("队列大小:"+queue.size());
        String dequeue = queue.dequeue();
        System.out.println("取元素:"+dequeue);
        System.out.println("取元素后队列的大小:"+queue.size());

    }
}
