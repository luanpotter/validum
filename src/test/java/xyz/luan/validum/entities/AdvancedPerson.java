package xyz.luan.validum.entities;

import xyz.luan.validum.validations.EnumExcept;
import xyz.luan.validum.validations.EnumOnly;

public class AdvancedPerson extends Person {

	@EnumOnly({ "strength", "flight", "telekinesis" })
    private Power power;
    
    @EnumExcept("kriptonite")
    private Weakness weakness;

    public AdvancedPerson(String name, int age, Address address, Power power, Weakness weakness) {
        super(name, age, address);
        this.power = power;
        this.weakness = weakness;
    }
    
    public Weakness getWeakness() {
        return this.weakness;
    }
    
    public Power getPower() {
        return this.power;
    }

}
