package com.itdom.decorator;

/**
 * 类图https://app.diagrams.net/#Hchenanddom%2FCharts%2Fmain%2Frepo%2F%E7%B1%BB%E5%9B%BE.drawio
 */
public class DecotatorTest {
    public static void main(String[] args) {
        FootballPlayer footballPlayer = new FootballPlayer();
        footballPlayer.setPerson(new CommonPerson());
        footballPlayer.sport();
    }
}
