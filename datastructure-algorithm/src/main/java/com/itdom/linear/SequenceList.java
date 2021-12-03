package com.itdom.linear;


import java.util.Iterator;
import java.util.function.Consumer;

public class SequenceList<T> implements Iterable<T> {
    //存储u元素的数组
    private T[] elements;
    //记录当前顺序表的元素个数
    private int N;


    public SequenceList(int capacity) {
        this.elements = (T[]) new Object[capacity];
        this.N = 0;
    }

    //将线性表置空
    public void clear() {
        this.N = 0;
    }

    //    判断线性表是否为空，是返回true，否返回false
    public boolean isEmpty() {
        return this.N == 0;
    }

    //获取线性表中元素的个数
    public int length() {
        return this.N;
    }

    //读取并返回线性表中的第i个元素的值
    public T get(int i) {
        return elements[i];
    }

    //在线性表的第i个元素之前插入一个置为t的数据元素
    public void insert(int i, T t) {
        if (i == elements.length) {
//            throw new RuntimeException("this list is full");
            resize(elements.length*2);
        }
        if (i < 0 || i > elements.length) {
            throw new RuntimeException("parameter illegal exception");
        }
        for (int index = this.N; index > i; index++) {
            elements[index] = elements[index - 1];
        }
        elements[i] = t;
        this.N++;
    }

    //向线性表中添加一个元素t
    public void insert(T t) {
        if (this.N == this.elements.length) {
//            throw new RuntimeException("the table is full");
            resize(elements.length*2);
        }
        this.elements[this.N++] = t;
    }
    //删除角标i处的结果


    public T remove(int i) {
        if (i < 0 || i > this.N) {
            throw new RuntimeException("current element not exists");
        }
        T result = this.elements[i];
        //将当前元素的的后面的元素都需要向前移动意味
        for (int index = i; index < this.N - 1; index++) {
            this.elements[index] = this.elements[index + 1];
        }
        this.N--;
        if (this.N>0&&this.N<elements.length/4){
            resize(this.elements.length/2);
        }
        return result;
    }

    //查找t元素第一次出现的位置
    public int indexOf(T t) {
        if (t == null) {
            throw new RuntimeException("parameter can not be null");
        }
        for (int index = 0; index < this.N; index++) {
            if (this.elements[index].equals(t)) {
                return index;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        SequenceList<String> stringSequenceList = new SequenceList<String>(10);
        stringSequenceList.insert("张三");
        stringSequenceList.insert("李四");
        stringSequenceList.insert("王五");
        stringSequenceList.insert("张柳");
       /* System.out.println("获取序列1处的结果:" + stringSequenceList.get(1));
        System.out.println("结果长度:" + stringSequenceList.length());
        System.out.println("删除序列0处的结果:" + stringSequenceList.remove(0));
        System.out.println("结果长度:" + stringSequenceList.length());
        stringSequenceList.clear();
        System.out.println("结果长度:" + stringSequenceList.length());
        */
        Iterator<String> iterator = stringSequenceList.iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            System.out.println(next);
        }



    }

    public Iterator<T> iterator() {
        return new CustomerIterator();
    }
    private class CustomerIterator implements Iterator{

        private int cusor;

        public CustomerIterator() {
            this.cusor=0;
        }

        public boolean hasNext() {
            return cusor<N;
        }

        public T next() {
            return elements[this.cusor++] ;
        }

        public void remove() {

        }

        public void forEachRemaining(Consumer action) {

        }
    }

    private void resize(int newSize){
        T[] temp = elements;
        elements = (T[])new Object[newSize];
        for (int i = 0; i < N; i++) {
            elements[i]=temp[i];
        }
    }
}
