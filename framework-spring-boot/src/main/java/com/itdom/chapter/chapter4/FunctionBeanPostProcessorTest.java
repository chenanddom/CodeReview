package com.itdom.chapter.chapter4;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.Assert;

public class FunctionBeanPostProcessorTest {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("bean1",Bean1.class);
        context.registerBean("bean2",Bean2.class);
        context.registerBean("bean3",Bean3.class);
        context.registerBean("bean4",Bean4.class);
        //String值注入需要使用到的处理器
        context.getDefaultListableBeanFactory().setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        /*
        如果不添加上面的注解自动连线候选解析器就会报错
        Caused by: org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'java.lang.String' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {@org.springframework.beans.factory.annotation.Value(value=${JAVA_HOME})}
         */
        // @Autowired @Value
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);
        //@Resource @PostConstruct @PreDestroy
        context.registerBean(CommonAnnotationBeanPostProcessor.class);


        //注册ConfigurationPropertiesBindingPostProcessor后置处理器
        ConfigurationPropertiesBindingPostProcessor.register(context.getDefaultListableBeanFactory());

        //初始化容器,执行beanfactory后处理器，添加bean后置处理器，初始化实例
//        System.out.println(context.getBean(ConfigurationPropertiesBindingPostProcessor.class));
        context.refresh();
        System.out.println(context.getBean(Bean4.class));
        //销毁容器
        context.close();


    }


}
