package com.itdom.facade;

import java.util.List;

/**
 * 外观类
 */
public class Project {
    /**
     * 项目名称
     */
    private String name;

    /**
     * 产品经理
     */
    private ProjectManager projectManager;

    /**
     * 开发经理
     */
    private DevelopManager developManager;

    /**
     * 开发攻城狮们
     */
    private List<DevelopmentEngineer> engineerList;

    /**
     * 测试攻城狮
     */
    private List<QualityAssurance> assuranceList;

    /**
     * 运维攻城狮
     */
    private List<Operations> operationsList;

    public Project(String name, ProjectManager projectManager, DevelopManager developManager, List<DevelopmentEngineer> engineerList, List<QualityAssurance> assuranceList, List<Operations> operationsList) {
        this.name = name;
        this.projectManager = projectManager;
        this.developManager = developManager;
        this.engineerList = engineerList;
        this.assuranceList = assuranceList;
        this.operationsList = operationsList;
    }

    /**
     * 开发过程
     */
    public void developProject() {
        System.out.println(this.name + " 项目启动...");
        System.out.println("-----------------------------");
        //指定计划
        projectManager.makeProjectPlan();

        //需求分析
        projectManager.analysisRequirement();

        //软件设计
        developManager.makeDevelopmentPlan();

        //程序编写
        engineerList.forEach(DevelopmentEngineer::develop);

        //软件测试
        assuranceList.forEach(QualityAssurance::test);

        //运行维护
        operationsList.forEach(Operations::operationAndMaintenance);

        System.out.println("-----------------------------");
        System.out.println(this.name + " 项目完成...");
    }
}
