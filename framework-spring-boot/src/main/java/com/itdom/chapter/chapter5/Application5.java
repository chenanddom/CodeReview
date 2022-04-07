package com.itdom.chapter.chapter5;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

public class Application5 {
    public static void main(String[] args) throws IOException {
        GenericApplicationContext genericApplicationContext = new GenericApplicationContext();
        genericApplicationContext.registerBean("config",Config.class);
        //注册的这个postprocessor主要是解析@componentScan @Bean @Import @ImportResource注解的类
//        genericApplicationContext.registerBean(ConfigurationClassPostProcessor.class);
        //MapperScanConfigurer是增强mapper接口的后置处理器
//        genericApplicationContext.registerBean(MapperScannerConfigurer.class,beanDefinition -> {
//            beanDefinition.getPropertyValues().add("basePackage","com.itdom.chapter.chapter5.component");
//        });

        for (String beanDefinitionName : genericApplicationContext.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }

//        ComponentScan componentScan = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);
//        if (componentScan!=null){
//            AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator();
//            for (String s : componentScan.basePackages()) {
//                System.out.println(s);
//                String path = "classpath*:"+s.replace(".","/")+"/**/*.class";
//                System.out.println(path);
//                CachingMetadataReaderFactory cachingMetadataReaderFactory = new CachingMetadataReaderFactory();
//                Resource[] resources = genericApplicationContext.getResources(path);
//                for (Resource resource : resources) {
//                    System.out.println(resource);
//                    MetadataReader reader = cachingMetadataReaderFactory.getMetadataReader(resource);
//                    System.out.println("类名: "+reader.getClassMetadata().getClassName()+" 是否加Annotation Component:"+reader.getAnnotationMetadata().hasAnnotation(Component.class.getName())+" 是否派生注解: "+reader.getAnnotationMetadata().hasMetaAnnotation(Component.class.getName()));
//                    AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
//                    if (annotationMetadata.hasAnnotation(Component.class.getName())
//                            || annotationMetadata.hasMetaAnnotation(Component.class.getName())) {
//                        AbstractBeanDefinition bd = BeanDefinitionBuilder
//                                .genericBeanDefinition(reader.getClassMetadata().getClassName())
//                                .getBeanDefinition();
//                        DefaultListableBeanFactory beanFactory = genericApplicationContext.getDefaultListableBeanFactory();
//                        String name = generator.generateBeanName(bd, beanFactory);
//                        beanFactory.registerBeanDefinition(name, bd);
//                    }
//                }
//            }
//        }
//        genericApplicationContext.registerBean(ComponentScanPostProcessor.class);

        CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
        MetadataReader metadataReader = factory.getMetadataReader(new ClassPathResource("com/itdom/chapter/chapter5/Config.class"));
        Set<MethodMetadata> annotatedMethods = metadataReader.getAnnotationMetadata().getAnnotatedMethods(Bean.class.getName());
        for (MethodMetadata method : annotatedMethods) {
            String initMethod = method.getAnnotationAttributes(Bean.class.getName()).get("initMethod").toString();
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
            builder.setFactoryMethodOnBean(method.getMethodName(), "config");
            builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
            /*
            设置属性的名称
             */
            if (!StringUtils.isEmpty(initMethod)){
                builder.setInitMethodName(initMethod);
            }
            AbstractBeanDefinition bd = builder.getBeanDefinition();
            genericApplicationContext.getDefaultListableBeanFactory().registerBeanDefinition(method.getMethodName(),bd);
        }




        genericApplicationContext.refresh();
        System.out.println("======================================================================");
        for (String beanDefinitionName : genericApplicationContext.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }






        genericApplicationContext.close();

    }
}
