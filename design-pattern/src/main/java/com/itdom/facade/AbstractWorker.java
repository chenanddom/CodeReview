package com.itdom.facade;
/**
 * 参与者抽象
 */
public abstract class AbstractWorker {
    /**
     * 姓名
     */
    private String name;

    /**
     * 职位
     */
    private String job;

    public AbstractWorker(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    @Override
    public String toString() {
        return "AbstractWorker{" +
                "name='" + name + '\'' +
                ", job='" + job + '\'' +
                '}';
    }
}
