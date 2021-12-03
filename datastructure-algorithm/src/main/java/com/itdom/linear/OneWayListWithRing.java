package com.itdom.linear;

public class OneWayListWithRing {
    public static void main(String[] args) throws Exception {
        Node<String> first = new Node<String>("aa", null);
        Node<String> second = new Node<String>("bb", null);
        Node<String> third = new Node<String>("cc", null);
        Node<String> fourth = new Node<String>("dd", null);
        Node<String> fifth = new Node<String>("ee", null);
        Node<String> six = new Node<String>("ff", null);
        Node<String> seven = new Node<String>("gg", null);
//完成结点之间的指向
        first.next = second;
        second.next = third;
        third.next = fourth;
        fourth.next = fifth;
        fifth.next = six;
        six.next = seven;
//产生环
        seven.next = third;
//判断链表是否有环
        boolean circle = isCircle(first);
        System.out.println("first链表中是否有环：" + circle);
        System.out.println("环的入口为:" + getEntrace(first).item);
    }

    /*
    当快慢指针相遇时，我们可以判断到链表中有环，这时重新设定一个新指针指向链表的起点，且步长与慢指针一样
为1，则慢指针与“新”指针相遇的地方就是环的入口
     */
    public static Node getEntrace(Node first) {
        Node<String> fast = first;
        Node<String> slow = first;
        //定义临时结点，为找到循环的入口
        Node<String> temp = null;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            //证明有环
            if (fast.equals(slow)) {
                temp = first;//定义临时结点指向首结点
                continue;
            }
            if (temp != null) {
                temp = temp.next;
                if (temp.equals(slow)) {
                    return temp;
                }
            }
        }
        return null;
    }


    /**
     * 判断链表中是否有环
     *
     * @param first 链表首结点
     * @return ture为有环，false为无环
     */
    public static boolean isCircle(Node<String> first) {
        //定义快慢指针
        Node<String> fast = first;
        Node<String> slow = first;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast.equals(slow)) {
                return true;
            }
        }
        return false;
    }

    //结点类
    private static class Node<T> {
        //存储数据
        T item;
        //下一个结点
        Node next;

        public Node(T item, Node next) {
            this.item = item;
            this.next = next;
        }
    }
}
