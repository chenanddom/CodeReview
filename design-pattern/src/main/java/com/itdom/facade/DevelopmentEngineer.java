package com.itdom.facade;

public class DevelopmentEngineer extends AbstractWorker{
    public DevelopmentEngineer(String name, String job) {
        super(name, "开发工程师");
    }
    public void develop() {
        System.out.println(super.getName() + " | " + super.getJob() + " 开始进行研发...");
    }
}
