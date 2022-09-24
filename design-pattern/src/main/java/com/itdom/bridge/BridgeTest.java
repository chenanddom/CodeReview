package com.itdom.bridge;

public class BridgeTest {
    public static void main(String[] args) {
        Fruit strawberry = new Strawberry();
        Food cake = new Cake();
        cake.setFruit(strawberry);
        cake.add();
        Food milkTea = new MilkTea();
        milkTea.setFruit(strawberry);
        milkTea.add();
    }
}
