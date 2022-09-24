package com.itdom.adapter.interfaceadapter;


public class ClassAdapterTest {
    public static void main(String[] args) {
        AC220V ac220V = new AC220V();
        Adapter110V adapter110V = new Adapter110V(ac220V);
        int i = adapter110V.outPu110V();
        System.out.println(i);
        Adapter5V adapter5V = new Adapter5V(ac220V);
        System.out.println(adapter5V.outPut5V());
    }
}
