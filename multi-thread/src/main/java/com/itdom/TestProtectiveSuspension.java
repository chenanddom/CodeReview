package com.itdom;

import com.itdom.utils.DownloadUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 保护性暂停
 * 同步模式之保护性暂停
 * 即Guarded Suspension，用在一个线程等待另一个线程的执行结果的时候。
 * 要点:
 * 1.有一个结果需要从一个线程传递到另一个线程，让它们关联同一个对象GuardObject
 * 2. 如果有结果不断从一个线程到另一个线程那么可以使用消息队列(生产者消费者)
 * 3. JDK中，join的是新，Future的实现，采用的就是此模式
 * 4. 因为需要等待另一个线程的结果，因此归类到同步模式。
 */
@Slf4j(topic = "c.TestProtectiveSuspension")
public class TestProtectiveSuspension {

    public static void main(String[] args) throws InterruptedException {
//        GuardedObject guardedObject = new GuardedObject();
//        //线程1等待线程2的执行结果
//        new Thread(()->{
//            //没有获取到结果就会阻塞等待
//            Object o = guardedObject.get(2000);
//            log.debug("result is{}",o);
//        },"t1").start();
//
//        new Thread(()->{
//            try {
//                log.debug("正在获取结果...");
//                String download = DownloadUtils.download();
//                guardedObject.complete(download);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        },"t2").start();

        for (int i = 0; i < 3; i++) {
            new People().start();
        }
        Thread.sleep(1000);
        for (Integer id : MainBoxes.getIds()) {
            new PostMan(id, "content " + id).start();
        }


    }
}

@Slf4j(topic = "c.People")
class People extends Thread {

    @Override
    public void run() {
        GuardedObject guardObject = MainBoxes.createGuardObject();
        log.debug("start receive message:{}", guardObject.getId());
        Object mail = guardObject.get(5000);
        log.debug("received message:{},content:{}", guardObject.getId(), mail);
    }
}

@Slf4j(topic = "c.PostMan")
class PostMan extends Thread {
    private int id;
    private String mail;

    public PostMan(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        GuardedObject guardObject = MainBoxes.getGuardObject(id);
        log.debug("send message id:{},content:{}", id, mail);
        guardObject.complete(mail);
    }
}

class MainBoxes {
    private static Map<Integer, GuardedObject> boxes = new ConcurrentHashMap<>();

    private static int id = 1;

    /**
     * 产生唯一的id
     *
     * @return
     */
    private static synchronized int generateId() {
        return id++;
    }

    public static GuardedObject getGuardObject(int id) {
        return boxes.remove(id);
    }

    public static GuardedObject createGuardObject() {
        GuardedObject guardedObject = new GuardedObject(generateId());
        boxes.put(guardedObject.getId(), guardedObject);
//        guardedObject.complete(guardedObject.getResponse());
        return guardedObject;
    }

    public static Set<Integer> getIds() {
        return boxes.keySet();
    }
}


class GuardedObject {
    private int id;


    //结果
    private Object response;

    public GuardedObject(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    //获取结果的方法
    public Object get(long timeout) {
        synchronized (this) {
            long beginTime = System.currentTimeMillis();
            long passedTime = 0;
                while (response == null) {

                    //还没有结果
                    //避免虚假唤醒出现的长时间等待
                    long waitTime = timeout - passedTime;
                    if (timeout - passedTime <= 0) {
                        break;
                    }
                    try {
                        this.wait(waitTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            return response;
        }
    }

    //产生结果
    public void complete(Object response) {
        synchronized (this) {
            this.response = response;
            this.notifyAll();
        }
    }
}