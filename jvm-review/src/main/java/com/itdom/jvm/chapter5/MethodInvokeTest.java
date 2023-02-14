package com.itdom.jvm.chapter5;

public class MethodInvokeTest {
    public static void main(String[] args) {
        System.out.println();
    }
}
class Animal{
    public void eat(){}
}
class Dog extends Animal{
    public Dog(){
        super();
    }
    public Dog(String name){
        this();
    }
    @Override
    public void eat() {
        super.eat();
    }
}