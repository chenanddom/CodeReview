package com.itdom.guava.objectmethod;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

import java.util.*;

public class CityByPopluation implements Comparator<City> {


    @Override
    public int compare(City o1, City o2) {
        return Ints.compare(o1.getPopulation(),o2.getPopulation());
    }

    public static void main(String[] args) {

        CityByPopluation cityByPopluation = new CityByPopluation();
        CityByRainfall cityByRainfall = new CityByRainfall();
        City city1 = new City("Beijing", 100000, 10.0);
        City city2 =  new City("Shanghai",100000,45.0);
        City city3 = new City("Shenzhen",100000,55.0);

        ArrayList<City> cities = Lists.newArrayList(city1, city2, city3);
        Ordering<City> firstOrdering = Ordering.from(cityByRainfall).reverse();
        Collections.sort(cities,firstOrdering);
        Iterator<City> cityByRainfallIterator = cities.iterator();
        while (cityByRainfallIterator.hasNext()){
            System.out.print(cityByRainfallIterator.next().getCityName()+"\t");
        }

        System.out.println("多参数排序");
        Ordering<City> secondOrdering = Ordering.from(cityByPopluation).compound(cityByRainfall);
        Collections.sort(cities,secondOrdering);
        Iterator<City> cityIterator = cities.iterator();
        while (cityIterator.hasNext()){
            City city = cityIterator.next();
            System.out.print(city.getCityName()+"\t");
        }
        System.out.println();

        //获取最大值
        Ordering<City> thirdOrdering = Ordering.from(cityByRainfall);
        List<City> cityList = thirdOrdering.greatestOf(cities, 2);
        Iterator<City> iterator = cityList.iterator();
        while (iterator.hasNext()){
            City next = iterator.next();
            System.out.print(next.getCityName()+"\t");
        }
        System.out.println();
      //获取最小值
        List<City> least = thirdOrdering.leastOf(cities, 1);
        Iterator<City> cityIterator1 = least.iterator();
        if (cityIterator1.hasNext()){
            System.out.println(cityIterator1.next().getCityName());
        }

    }

}
