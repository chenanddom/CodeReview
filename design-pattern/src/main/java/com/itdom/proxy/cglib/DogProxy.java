package com.itdom.proxy.cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class DogProxy {
    public static void main(final String[] args) {
        final Dog dog = new Dog();
        final AnimalAdvice animalAdvice = new AnimalAdvice();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(dog.getClass());
        enhancer.setCallback(new MethodInterceptor() {
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                animalAdvice.before();
                Object result = method.invoke(dog, args);
                animalAdvice.after();
                return result;
            }
        });

        Dog proxtDog = (Dog)enhancer.create();
        proxtDog.eat();
    }


}
