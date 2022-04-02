package com.itdom.chapter.chapter3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class TestBeanLefeCycle {
    private static final Logger logger = LoggerFactory.getLogger(TestBeanLefeCycle.class);
    public TestBeanLefeCycle() {
        logger.debug("构造方法...");
    }

    /**
     * 依赖注入的方法
     * @param home
     */
    @Autowired
    public void autowire(@Value("${JAVA_HOME}")String home){
        logger.debug("依赖注入：{}",home);
    }

    /**
     * 初始化方法
     */
    @PostConstruct
    public void init(){
        logger.debug("初始化...");
    }

    /**
     * 销毁的方法
     */
    @PreDestroy
    public void destroy(){
        logger.debug("销毁...");
    }
}
