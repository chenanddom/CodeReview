package com.itdom.jvm.chapter5;

public class ReuseSlotTest {
    public void method1(){
        int a=0;
        System.out.println(a);
        int b=0;
    }
    public void method2(){
        {
            int a=0;
            System.out.println(a);
        }
        int b=0;
    }

    public static void main(String[] args) {
        new ReuseSlotTest().method2();
    }
}
