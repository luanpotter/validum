package xyz.luan.sabv.entities;

import xyz.luan.sabv.validations.Numeric;
import xyz.luan.sabv.validations.Numeric.Cap;
import xyz.luan.sabv.validations.Numeric.Type;
import xyz.luan.sabv.validations.Required;

public class Person {

    @Required
    private String name;

    @Numeric(min = 0, minCap = Cap.INCLUSIVE, maxCap = Cap.NONE, type = Type.INTEGER)
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
