package com.itdom.stackframe;

public class StackFrame {

    public static void main(String[] args) {
        method1();
    }

    public static void method1(){
        int i=100;
        System.out.println(i);
        method2(i);
        System.out.println(i);
    }
    public static int method2(int i){
        int step=2;
        i+=step;
        return i;
    }

}
