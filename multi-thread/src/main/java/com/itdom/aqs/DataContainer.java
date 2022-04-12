package com.itdom.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 测试读写锁实现缓存
 * ReentrantReadWriteLock
 */
@Slf4j(topic = "c.DataContainer")
public class DataContainer {
    private ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock readLock = rw.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = rw.writeLock();

    public static void main(String[] args) {
        DataContainer dataContainer = new DataContainer();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        fixedThreadPool.submit(() -> {
            for (int i = 0; i < 100; i++) {
                dataContainer.read();
            }
        });
        fixedThreadPool.submit(() -> {
            for (int i = 0; i < 100; i++) {
                dataContainer.read();
            }
        });
        fixedThreadPool.submit(() -> {
            for (int i = 0; i < 100; i++) {
                dataContainer.write();
            }
        });
    }

    public Object read() {
        readLock.lock();
        try {
            log.debug("开始读取...");
            Thread.sleep(100);
            log.debug("读取结束...");
            return "返回读取结果";
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
        return null;
    }

    public void write() {
        writeLock.lock();
        try {
            log.debug("开始写入...");
            Thread.sleep(1000);
            log.debug("写入结束...");
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

}
