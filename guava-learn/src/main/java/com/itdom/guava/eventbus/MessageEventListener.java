package com.itdom.guava.eventbus;

import com.google.common.eventbus.Subscribe;

public class MessageEventListener {
    @Subscribe
    public void messageEventListener(Message<String> messageEvent) {
        System.out.println(messageEvent.getMsg());
    }

}
