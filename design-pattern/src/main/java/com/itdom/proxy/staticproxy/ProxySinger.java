package com.itdom.proxy.staticproxy;

public class ProxySinger implements ISinger{
    private Singer singer;

    public ProxySinger(Singer singer) {
        this.singer = singer;
    }

    @Override
    public void sing() {
        System.out.println("明星人设打造");
        singer.sing();
        System.out.println("明星人设维护公关");

    }
}
