package com.itdom.guava.eventbus;

import com.google.common.eventbus.EventBus;

public class EventBusTest {
    public static void main(String[] args) {
        EventBus bus = new EventBus();
        bus.register(new MessageEventListener());
        bus.post(new Message<String>("test message"));
    }
}
