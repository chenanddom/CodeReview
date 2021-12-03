package com.itdom.linear;

import java.util.Iterator;

/**
 * 1.private Node head:记录首结点
 * 2.private int N:当前栈的元素个数
 *
 * 1.public boolean isEmpty()：判断栈是否为空，是返回true，否返回false
 * 2.public int size():获取栈中元素的个数
 * 3.public T pop():弹出栈顶元素
 * 4.public void push(T t)：向栈中压入元素t
 * @param <T>
 */
public class Stack<T> implements Iterable<T>  {
    //记录首结点
    Node head;
    //栈中元素个数
    int N;

    public Stack() {
        head = new Node(null,null);
        this.N=0;
    }
    public boolean isEmpty(){
        return this.N==0;
    }

    public int size(){
        return this.N;
    }
    public T pop(){
        Node node = this.head.next;
        if (node==null){
            return null;
        }
        this.head.next=node.next;
        this.N--;
        return (T) node.item;
    }
    public void push(T t){
        Node oldNode = head.next;
        Node node = new Node(t,oldNode);
        head.next=node;
        this.N++;

    }
    @Override
    public Iterator<T> iterator() {
        return new CustomerIterator();
    }

    private static class Node<T>{
        T item;
        private Node next;

        public Node(T item, Node next) {
            this.item = item;
            this.next = next;
        }
    }



    private class CustomerIterator implements Iterator{

        private Node h=head;
        @Override
        public boolean hasNext() {
            return h.next!=null;
        }

        @Override
        public Object next() {
            Node currentNode = h.next;
            h=currentNode;
            return currentNode.item;
        }
    }

    public static void main(String[] args) {
        Stack<String> stack = new Stack<>();
        stack.push("1");
        stack.push("2");
        stack.push("3");
        stack.push("4");
        stack.push("5");
        System.out.println(stack.size());
        Iterator<String> iterator = stack.iterator();
        while (iterator.hasNext()){
            System.out.print(iterator.next()+",");
        }
        System.out.println();
        String data = stack.pop();
        System.out.println(data);
        System.out.println(stack.size());
    }
}
