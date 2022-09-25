package com.itdom.proxy.staticproxy;

public class ProxyTest {
    public static void main(String[] args) {
        ISinger iSinger = new ProxySinger(new Singer());
        iSinger.sing();
    }
}
