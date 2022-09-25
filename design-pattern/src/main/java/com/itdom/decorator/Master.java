package com.itdom.decorator;

public class Master extends Person {
    private Person person;

    public void setPerson(Person person) {
        this.person = person;
    }

    public void sport() {
        person.sport();
    }
}
