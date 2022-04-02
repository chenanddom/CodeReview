package com.itdom.chapter.chapter1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;

/**
 * BeanFactory容器的特点
 */
public class TestBeanFactory {

    public static void main(String[] args) {
        //初创的DefaultListableBeanFactory是没有任何的bean的定义的
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //Bean的定义主要包括(class,scope,初始化，销毁),此处是手动创建bean的定义并且注册
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Config.class).setScope("singleton").getBeanDefinition();
        //注册benadefinition
        beanFactory.registerBeanDefinition("config",beanDefinition);

        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
        System.out.println("================================================================================================================================================================");
        //给beanFactory添加常用的后置处理器,补充了一些bean的定义
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        //此处会多出一些后置处理的bean
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
        System.out.println("================================================================================================================================================================");

        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        beanFactoryPostProcessorMap.values().stream().forEach(e->{
            //执行beanpostprocessor
            e.postProcessBeanFactory(beanFactory);
        });

        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
        System.out.println("================================================================================================================================================================");
        /*
        bean的后处理，针对bean的生命周期各个阶段进行扩展，例如@Autowired@Resource...
         */
        Collection<BeanPostProcessor> beanPostProcessors = beanFactory.getBeansOfType(BeanPostProcessor.class).values();
        beanPostProcessors.stream().sorted(beanFactory.getDependencyComparator()).forEach(e->{
            System.out.println(">>>>"+e);
            beanFactory.addBeanPostProcessor(e);
        });
        //预先创建bean
        beanFactory.preInstantiateSingletons();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(beanFactory.getBean(Bean1.class).getBean2());

        System.out.println("================================================================================================================================================================");

        System.out.println(beanFactory.getBean(Bean1.class).getInter());


    }
    @Configuration
    static class Config {
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }

        @Bean
        public Bean3 bean3() {
            return new Bean3();
        }

        @Bean
        public Bean4 bean4() {
            return new Bean4();
        }
    }

    interface Inter {

    }

    static class Bean3 implements Inter {

    }

    static class Bean4 implements Inter {

    }

    static class Bean1 {
        private static final Logger log = LoggerFactory.getLogger(Bean1.class);

        public Bean1() {
            log.debug("构造 Bean1()");
        }

        @Autowired
        private Bean2 bean2;

        public Bean2 getBean2() {
            return bean2;
        }

        @Autowired
        @Resource(name = "bean4")
        private Inter bean3;

        public Inter getInter() {
            return bean3;
        }
    }

    static class Bean2 {
        private static final Logger log = LoggerFactory.getLogger(Bean2.class);

        public Bean2() {
            log.debug("构造 Bean2()");
        }
    }
}
