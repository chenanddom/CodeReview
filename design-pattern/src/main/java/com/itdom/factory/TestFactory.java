package com.itdom.factory;

public class TestFactory {
    public static void main(String[] args) {
        CarFactory carFactory = new GeelyCarFactory();
        Car car = carFactory.createCar();
        car.run();
        CarFactory factory = new GrateWallCarFactory();
        Car car2 = factory.createCar();
        car2.run();
    }
}
