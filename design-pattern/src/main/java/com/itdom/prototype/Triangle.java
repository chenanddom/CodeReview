package com.itdom.prototype;

public class Triangle implements Shape,Cloneable{
    public void show() {
        System.out.println("this is Triangle...");
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
