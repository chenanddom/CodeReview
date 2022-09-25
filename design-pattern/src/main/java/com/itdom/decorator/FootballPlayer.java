package com.itdom.decorator;


public class FootballPlayer extends Master {
    @Override
    public void sport() {
        super.sport();
        beGoodAt();
    }
    public void beGoodAt(){
        System.out.println("各种踢足球球技...");
    }
}
