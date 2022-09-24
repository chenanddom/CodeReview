package com.itdom.adapter.interfaceadapter;

public class Adapter5V extends PowerAdapter {
    public Adapter5V(AC220V ac220V) {
        super(ac220V);
    }

    public int outPut5V() {
        int output = 0;
        if (getAc220V() != null) {
            output = getAc220V().output220V() / 44;
        }
        return output;
    }
}
