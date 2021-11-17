package com.itdom.guava.eventbus;

public class Message<T> {
    private T msg;

    public Message() {
    }

    public Message(T msg) {
        this.msg = msg;
    }

    public T getMsg() {
        return msg;
    }

    public void setMsg(T msg) {
        this.msg = msg;
    }
}

