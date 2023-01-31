package com.itdom.jvm.chapter5;

/**
 * 测试栈设置栈大小-Xss256k
 * 默认情况打印到11421
 * 设置了虚拟机器参数之后打印数据就到了2462，说明虚拟机参数设置生效了.
 */
public class StackTest {
    public static int num=1;
    public static void main(String[] args) {
        System.out.println(num);
        num++;
        main(args);
    }
}
