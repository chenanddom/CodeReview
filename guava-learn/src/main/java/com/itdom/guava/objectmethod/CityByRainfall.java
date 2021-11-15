package com.itdom.guava.objectmethod;

import java.util.Comparator;

public class CityByRainfall implements Comparator<City> {
    @Override
    public int compare(City o1, City o2) {
        return Double.compare(o1.getAverageRainFall(),o2.getAverageRainFall());
    }
}
