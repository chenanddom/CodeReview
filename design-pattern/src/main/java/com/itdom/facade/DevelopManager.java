package com.itdom.facade;

public class DevelopManager extends AbstractWorker{
    public DevelopManager(String name) {
        super(name, "开发经理");
    }
    public void makeDevelopmentPlan() {
        System.out.println(super.getName() + " | " + super.getJob() + " 制定了研发计划...");
    }

}
