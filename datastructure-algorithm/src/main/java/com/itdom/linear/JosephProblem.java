package com.itdom.linear;

import javax.xml.soap.Node;

/**
 * 约瑟夫问题
 */
public class JosephProblem {
    public static void main(String[] args) {
        //定义首结点
        Node<Integer> first = null;
        //定义当前结点的前一个结点
        Node<Integer> pre = null;

        //构建循环链表
        for (int i = 1; i <= 41; i++) {
            if (i == 1) {
                first = new Node<>(i, null);
                pre = first;
                continue;
            }
            Node<Integer> node = new Node<>(i, null);
            pre.next = node;
            pre = node;
            if (i == 41) {
                pre.next = first;
            }
        }
        //记录报数的值
        int count = 0;
        //
        Node<Integer> curr = first;
        Node<Integer> before = null;
        while (curr != curr.next) {
            count++;
            if (count == 3) {
                before.next = curr.next;
                System.out.print(curr.item + ",");
                curr = curr.next;
                count=0;
            } else {
                before = curr;
                curr = curr.next;
            }
        }
        System.out.println();
        System.out.println(curr.item);
    }
    private static class Node<T> {
        T item;
        private Node next;

        public Node(T item, Node next) {
            this.item = item;
            this.next = next;
        }
    }

}
