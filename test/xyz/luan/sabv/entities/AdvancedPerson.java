package xyz.luan.sabv.entities;

public class AdvancedPerson extends Person {

    private Power power;
    
    public AdvancedPerson(String name, int age, Address address, Power power) {
        super(name, age, address);
        this.power = power;
    }
    
    public Power getPower() {
        return this.power;
    }

}
