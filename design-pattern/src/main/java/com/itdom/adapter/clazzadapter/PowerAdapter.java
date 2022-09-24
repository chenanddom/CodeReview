package com.itdom.adapter.clazzadapter;

public class PowerAdapter extends AC220V implements DC110V{
    public int output110V() {
        int i = super.output220V();
        return i/2;
    }
}
