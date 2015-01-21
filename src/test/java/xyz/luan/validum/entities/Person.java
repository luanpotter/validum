package xyz.luan.validum.entities;

import xyz.luan.validum.validations.Numeric;
import xyz.luan.validum.validations.Required;
import xyz.luan.validum.validations.Numeric.Cap;
import xyz.luan.validum.validations.Numeric.Type;

public class Person {

    @Required
    private String name;

    @Numeric(min = 0, minCap = Cap.INCLUSIVE, maxCap = Cap.NONE, type = Type.INTEGER)
    private int age;

    protected Address address;
    
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
