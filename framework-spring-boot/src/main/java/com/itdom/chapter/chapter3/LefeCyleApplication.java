package com.itdom.chapter.chapter3;

import com.itdom.event.UserLoginEvent;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

@SpringBootApplication
public class LefeCyleApplication {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException {
        // BeanFactory实际上是核心的容器，applicationContext也是间接的继承了它。
        ConfigurableApplicationContext context = SpringApplication.run(LefeCyleApplication.class, args);
        context.close();

    }
}
