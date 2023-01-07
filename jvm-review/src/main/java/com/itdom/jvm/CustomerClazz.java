package com.itdom.jvm;

public class CustomerClazz {
    public static int n=1;//prepare环节只会赋值为0值--->initing(初始化阶段)才将n=1
    static {
        n=20;
    }


    public static void main(String[] args) {
        System.out.println(n);



    }
}
