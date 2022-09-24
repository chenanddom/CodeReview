package com.itdom.bridge;

public abstract class Food {

    private Fruit fruit;

    public void setFruit(Fruit fruit) {
        this.fruit = fruit;
    }

    public Fruit getFruit() {
        return fruit;
    }

    public abstract void add();

}
