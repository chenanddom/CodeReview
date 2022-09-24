package com.itdom.adapter.objectadapter;

import com.itdom.adapter.clazzadapter.DC110V;

public class PowerAdapter implements DC110V {
    private AC220V ac220V;

    public int output110V() {
        int i = this.ac220V.output220V();
        return i/2;
    }

    public void setAc220V(AC220V ac220V) {
        this.ac220V = ac220V;
    }
}
