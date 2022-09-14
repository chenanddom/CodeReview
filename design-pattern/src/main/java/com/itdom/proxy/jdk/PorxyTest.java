package com.itdom.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class PorxyTest implements InvocationHandler {

    private AnimalAdvice advice;
    private Animal dog;

    public PorxyTest(AnimalAdvice advice, Animal dog) {
        this.advice = advice;
        this.dog = dog;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        advice.before();
        Object invoke = method.invoke(dog, args);
        advice.after();
        return invoke;
    }

    public static void main(String[] args) {
        AnimalAdvice animalAdvice = new AnimalAdvice();
        Animal dog = new Dog();
        System.out.println(dog.getClass().getName());
        PorxyTest handler = new PorxyTest(animalAdvice, dog);

        Animal animal = (Animal) Proxy.newProxyInstance(dog.getClass().getClassLoader(), dog.getClass().getInterfaces(), handler);
        animal.eat();
        System.out.println(animal.getClass().getName());

    }

}
