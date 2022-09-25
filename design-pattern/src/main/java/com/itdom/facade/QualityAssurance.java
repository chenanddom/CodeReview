package com.itdom.facade;

public class QualityAssurance extends AbstractWorker{
    public QualityAssurance(String name) {
        super(name, "测试攻城狮");
    }
    public void test() {
        System.out.println(super.getName() + " | " + super.getJob() + " 开始进行测试...");
    }
}
