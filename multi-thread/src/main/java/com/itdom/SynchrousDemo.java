package com.itdom;

public class SynchrousDemo {
    static final Object lock = new Object();
    static int counter=0;
    public static void main(String[] args) {
        synchronized (lock){
        counter++;
        }
    }
}