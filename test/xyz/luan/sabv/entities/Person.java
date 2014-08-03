package xyz.luan.sabv.entities;

import xyz.luan.sabv.validations.Required;

public class Person {

    @Required
    private String name;

    private int age;

    private Address address;
    
    public Person(String name, int age, Address address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Address getAddress() {
        return address;
    }
}
