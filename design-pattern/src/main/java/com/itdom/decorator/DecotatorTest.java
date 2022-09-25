package com.itdom.decorator;

public class DecotatorTest {
    public static void main(String[] args) {
        FootballPlayer footballPlayer = new FootballPlayer();
        footballPlayer.setPerson(new CommonPerson());
        footballPlayer.sport();
    }
}
