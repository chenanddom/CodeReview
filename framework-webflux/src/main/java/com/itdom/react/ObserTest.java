package com.itdom.react;

import java.util.Observable;

public class ObserTest extends Observable {
    public static void main(String[] args) {
        ObserTest obserTest = new ObserTest();
        obserTest.addObserver((o,arg)->{
            System.out.println("发生变化!");
        });
        obserTest.addObserver((o,arg)->{
            System.out.println("被观察者有异动！！！");
        });
        obserTest.setChanged();//发生了变化
        obserTest.notifyObservers();//通知变化
    }
}
