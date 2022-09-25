package com.itdom.proxy.staticproxy;

public class Singer implements ISinger{
    @Override
    public void sing() {
        System.out.println("纯粹的唱歌");
    }
}
