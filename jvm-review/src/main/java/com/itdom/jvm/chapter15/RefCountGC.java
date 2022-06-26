package com.itdom.jvm.chapter15;

/**
 * jvm并没有使用引用计数算法进行垃圾回收
 */
public class RefCountGC {
    /**
     * 5M内存分配
     */
    private byte[] bigSize = new byte[1024*1024*5];
    Object reference=null;


    public static void main(String[] args) {
        RefCountGC aRef = new RefCountGC();
        RefCountGC bRef = new RefCountGC();
        aRef.reference = bRef;
        bRef.reference=aRef;
        /**
         * 引用置空
         */
        aRef = null;
        bRef = null;
        System.gc();
    }
}
