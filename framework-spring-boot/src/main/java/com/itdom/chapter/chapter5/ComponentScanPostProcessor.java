package com.itdom.chapter.chapter5;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class ComponentScanPostProcessor implements BeanDefinitionRegistryPostProcessor {
    /**
     * context调用reflush时调用
     * @param configurableListableBeanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        ComponentScan componentScan = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);
        try {
            if (componentScan != null) {
                AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator();
                for (String s : componentScan.basePackages()) {
                    System.out.println(s);
                    String path = "classpath*:" + s.replace(".", "/") + "/**/*.class";
                    System.out.println(path);
                    CachingMetadataReaderFactory cachingMetadataReaderFactory = new CachingMetadataReaderFactory();
                    Resource[] resources = new Resource[0];
                    resources = new PathMatchingResourcePatternResolver().getResources(path);
                    for (Resource resource : resources) {
                        System.out.println(resource);
                        MetadataReader reader = cachingMetadataReaderFactory.getMetadataReader(resource);
                        System.out.println("类名: " + reader.getClassMetadata().getClassName() + " 是否加Annotation Component:" + reader.getAnnotationMetadata().hasAnnotation(Component.class.getName()) + " 是否派生注解: " + reader.getAnnotationMetadata().hasMetaAnnotation(Component.class.getName()));
                        AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
                        if (annotationMetadata.hasAnnotation(Component.class.getName())
                                || annotationMetadata.hasMetaAnnotation(Component.class.getName())) {
                            AbstractBeanDefinition bd = BeanDefinitionBuilder
                                    .genericBeanDefinition(reader.getClassMetadata().getClassName())
                                    .getBeanDefinition();
                            String name = generator.generateBeanName(bd, beanDefinitionRegistry);
                            beanDefinitionRegistry.registerBeanDefinition(name, bd);
                        }
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}