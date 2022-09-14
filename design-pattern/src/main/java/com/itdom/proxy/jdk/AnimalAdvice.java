package com.itdom.proxy.jdk;

public class AnimalAdvice {
    public void before(){
        System.out.println("advice before...");
    }
    public void after(){
        System.out.println("advice after...");
    }
}
