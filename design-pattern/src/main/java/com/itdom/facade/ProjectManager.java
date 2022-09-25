package com.itdom.facade;

public class ProjectManager extends AbstractWorker{
    public ProjectManager(String name) {
        super(name, "项目经理");
    }
    public void makeProjectPlan() {
        System.out.println(super.getName() + " | " + super.getJob() + " 制定了项目计划...");
    }

    public void analysisRequirement() {
        System.out.println(super.getName() + " | " + super.getJob() + " 进行了需求分析...");
    }
}
