package com.itdom.adapter.clazzadapter;

public class ClassAdapterTest {
    public static void main(String[] args) {
        PowerAdapter adapter = new PowerAdapter();
        int i = adapter.output110V();
        System.out.println(i);
    }
}
