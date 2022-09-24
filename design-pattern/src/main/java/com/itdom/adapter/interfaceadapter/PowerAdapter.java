package com.itdom.adapter.interfaceadapter;

public class PowerAdapter implements DCOutput{
        private AC220V ac220V;

    public PowerAdapter(AC220V ac220V) {
        this.ac220V = ac220V;
    }

    public int output5V() {
        return ac220V.output220V();
    }

    public int output10V() {
        return ac220V.output220V();
    }

    public AC220V getAc220V() {
        return ac220V;
    }
}
