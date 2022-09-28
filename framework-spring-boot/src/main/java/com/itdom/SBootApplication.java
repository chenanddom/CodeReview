package com.itdom;

import com.itdom.event.UserLoginEvent;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

@SpringBootApplication
@MapperScan(basePackages = {"com.itdom.dao"})
public class SBootApplication {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException {
        // BeanFactory实际上是核心的容器，applicationContext也是间接的继承了它。
        ConfigurableApplicationContext context = SpringApplication.run(SBootApplication.class, args);
//        Object controller = context.getBean("controller");
//        System.out.println(controller);
//        //BeanFactory是核心容器，它的功能主要是由实现类来实现的，主要包括了控制反转，依赖注意，Bean的生命周期的管理等
//        //BeanFactory的默认实现类是DefaultListableBeanFatory;
//        Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
//        singletonObjects.setAccessible(true);
//        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
//        Map<String, Object> map = (Map<String, Object>)singletonObjects.get(beanFactory);
//        map.forEach((k,v)->{
//            System.out.println(k+"="+v);
//        });
//        //1.applicationContext就是相对与BeanFactory还融合了其他接口的功能，就是相当于既融合了BeanFactory接口里面声明的接口也融合了
//        // 主要包括了国际化，通配符匹配
//        System.out.println(context.getMessage("hi", null, Locale.CHINA));
//        System.out.println(context.getMessage("hi", null, Locale.JAPAN));
//        System.out.println(context.getMessage("hi", null, Locale.US));
//        //2.使用通配符获取资源
//        Resource[] resources = context.getResources("classpath:application.yml");
//        for (Resource resource : resources) {
//
//        System.out.println(resource);
//        }
//        // 通过匹配能够获取到jar里面的文件
//        Resource[] resources2 = context.getResources("classpath*:META-INF/spring.factories");
//        for (Resource resource : resources2) {
//            System.out.println(resource);
//        }
//        /*
//        3. EnvironmentCapable获取环境变量
//         */
//        String javaHome = context.getEnvironment().getProperty("JAVA_HOME");
//        String serverPort = context.getEnvironment().getProperty("server.port");
//        System.out.println(javaHome);
//        System.out.println(serverPort);
//        /*
//        4.发布事件,实现组件之间的耦合
//         */
//        context.publishEvent(new UserLoginEvent(context,"张三登录了"));


    }
}
