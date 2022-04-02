package com.itdom.chapter.chapter2;

import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.Controller;

public class TestApplicationContext {
    public static void main(String[] args) {
        testAnnotationConfigServletWebServerApplicationContext();
//        testAnnotationConfigApplicationContext();
//        testFileSystemXmlApplicationContext();
//        testClassPathXmlApplicationContext();
    }
    // ⬇️较为经典的容器, 基于 classpath 下 xml 格式的配置文件来创建
    private static void testClassPathXmlApplicationContext() {

        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("b01.xml");
        for (String name : classPathXmlApplicationContext.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        Bean2 bean2 = classPathXmlApplicationContext.getBean(Bean2.class);
        System.out.println(bean2.getBean1());
    }

    // ⬇️基于磁盘路径下 xml 格式的配置文件来创建
    private static void testFileSystemXmlApplicationContext() {
        FileSystemXmlApplicationContext fileSystemXmlApplicationContext =
                new FileSystemXmlApplicationContext("E:\\book\\CodeReview\\framework-spring-boot\\src\\main\\resources\\b01.xml");
        for (String name : fileSystemXmlApplicationContext.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        Bean2 bean = fileSystemXmlApplicationContext.getBean(Bean2.class);
        System.out.println(bean.getBean1());
    }

    // ⬇️较为经典的容器, 基于 java 配置类来创建
    private static void testAnnotationConfigApplicationContext() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(Config.class);
        for (String name : annotationConfigApplicationContext.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        Bean2 bean = annotationConfigApplicationContext.getBean(Bean2.class);
        System.out.println(bean.getBean1());
    }

    // ⬇️较为经典的容器, 基于 java 配置类来创建, 用于 web 环境
    private static void testAnnotationConfigServletWebServerApplicationContext() {
//        AnnotationConfigServletWebServerApplicationContext annotationConfigServletWebServerApplicationContext
//                = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);

    }

//    @Configuration
//    static class WebConfig {
//        //创建web服务的容器的工厂
//        @Bean
//        public ServletWebServerFactory servletWebServerFactory(){
//            return new TomcatServletWebServerFactory();
//        }
//        //创建前置服务器(处理所有路径的请求)
//        @Bean
//        public DispatcherServlet dispatcherServlet() {
//            return new DispatcherServlet();
//        }
//        //将前置控制器注册到web容器
//        @Bean
//        public DispatcherServletRegistrationBean registrationBean(DispatcherServlet dispatcherServlet) {
//            return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
//        }
//        @Bean("/hello")
//        public Controller controller1() {
//            return (request, response) -> {
//                response.getWriter().print("hello");
//                return null;
//            };
//        }
//    }

    @Configuration
    static class Config {
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2(Bean1 bean1) {
            Bean2 bean2 = new Bean2();
            bean2.setBean1(bean1);
            return bean2;
        }
    }

    static class Bean1 {
    }

    static class Bean2 {

        private Bean1 bean1;

        public void setBean1(Bean1 bean1) {
            this.bean1 = bean1;
        }

        public Bean1 getBean1() {
            return bean1;
        }
    }



}
