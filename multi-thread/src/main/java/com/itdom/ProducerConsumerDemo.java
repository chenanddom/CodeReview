package com.itdom;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
@Slf4j(topic = "c.ProducerConsumerDemo")
public class ProducerConsumerDemo {
    public static void main(String[] args) throws InterruptedException {
        MessageQueue messageQueue = new MessageQueue(2);

        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(()->{
                    messageQueue.put(new Message(id,"value:"+id));
            },"producer").start();
        }
        Thread.sleep(1000);
        new Thread(()->{
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message take = messageQueue.take();
            }
        },"consumer").start();

    }
}

/**
 * 消息队列类，java线程之间通信
 */
@Slf4j(topic = "c.MessageQueue")
class MessageQueue {
    //消息队列的集合
    private LinkedList<Message> list = new LinkedList<>();
    //消息队列的容量
    private int capacity;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    //获取消息
    public Message take() {
        //检查队列是否为空
        synchronized (list) {
            while (list.isEmpty()) {
                try {
                    log.debug("queue is empty consumer thread wait");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            /**
             * 在获取消息返回之前唤醒添加消息的线程添加消息
             */
            Message message = list.removeFirst();
            log.debug("received message {}",message);
            list.notifyAll();
            return message;
        }
    }

    public void put(Message message) {

        synchronized (list){
            /**
             * 检查队列是否已满
             */
            while (list.size()==capacity){
                try {
                    log.debug("queue is full producer thread wait");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            /**
             * 将消息加入到尾部
             */
            list.addLast(message);

            log.debug("message is created...");

            /**
             * 将等待线程消息的线程唤醒
             */
            list.notifyAll();
        }
    }
}
@Slf4j(topic = "c.Message")
class Message {
    private int id;
    private Object value;

    public Message(int id, Object value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}
