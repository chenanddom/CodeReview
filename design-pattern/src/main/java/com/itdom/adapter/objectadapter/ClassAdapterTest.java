package com.itdom.adapter.objectadapter;


public class ClassAdapterTest {
    public static void main(String[] args) {
        AC220V ac220V = new AC220V();
        PowerAdapter adapter = new PowerAdapter();
        adapter.setAc220V(ac220V);
        int i = adapter.output110V();
        System.out.println(i);
    }
}
