package com.itdom;

import java.io.Serializable;
//加锁final，防止使用继承的方式可以实现多实例。
public final class DclProblemDemo implements Serializable{
    private static DclProblemDemo INSTANCE = null;
   //私有的构造函数防止其他人可以创建多个实例
    private DclProblemDemo() {
    }
    //提供懒惰的初始化
    public static DclProblemDemo getInstance(){
        if (INSTANCE==null){
            synchronized (DclProblemDemo.class){
                if (INSTANCE==null){
                    INSTANCE=new DclProblemDemo();
                }
            }
        }
        return INSTANCE;
    }
    //防止如果结果实现了序列化的接口，能够实现反序列化。
    public Object readResolve(){
        return INSTANCE;
    }




}
