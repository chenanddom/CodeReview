package com.itdom.chapter.chapter3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
@Component
public class MyBeanPostProcessor implements InstantiationAwareBeanPostProcessor, DestructionAwareBeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(MyBeanPostProcessor.class);


    @Override
    public void postProcessBeforeDestruction(Object o, String s) throws BeansException {
        if (s.equalsIgnoreCase("testBeanLefeCycle")) {
            logger.debug("<<<<<<销毁前执行，如@preDestroy");
        }
    }


    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (beanName.equalsIgnoreCase("testBeanLefeCycle")) {
            logger.debug("<<<<<<实例化前执行，这里返回的对象会替换掉原本的 bean");

        }
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if (beanName.equalsIgnoreCase("testBeanLefeCycle")) {
            logger.debug("<<<<<<实例化后执行，这里如果返回 false 会跳过依赖注入阶段");

        }
        return true;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if (beanName.equalsIgnoreCase("testBeanLefeCycle")) {
            logger.debug("<<<<<<依赖注入阶段执行，如 @Autowired、@Value、@Resource");
        }
        return pvs;
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equalsIgnoreCase("testBeanLefeCycle")) {
            logger.debug("<<<<<<初始化之前执行，这里返回的对象会替换原来的bean，如@PostConstruct,@ConfigurationProperties");
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equalsIgnoreCase("testBeanLefeCycle")) {
            logger.debug("<<<<<<初始化之后执行，这里返回的对象会替换原来的bean，如代理增强");
        }
        return bean;
    }
}
