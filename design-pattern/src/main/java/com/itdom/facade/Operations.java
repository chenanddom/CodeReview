package com.itdom.facade;

public class Operations extends AbstractWorker {
    public Operations(String name) {
        super(name, "运维攻城狮");
    }
    public void operationAndMaintenance() {
        System.out.println(super.getName() + " | " + super.getJob() + " 持续进行运维...");
    }
}
