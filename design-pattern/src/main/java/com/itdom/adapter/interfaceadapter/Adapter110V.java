package com.itdom.adapter.interfaceadapter;

public class Adapter110V extends PowerAdapter {
    public Adapter110V(AC220V ac220V) {
        super(ac220V);
    }

    public int outPu110V() {
        int output = 0;
        if (getAc220V() != null) {
            output = getAc220V().output220V() / 2;
        }
        return output;
    }
}
