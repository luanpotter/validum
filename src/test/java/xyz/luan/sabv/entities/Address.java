package xyz.luan.sabv.entities;

import xyz.luan.sabv.validations.Numeric.Natural;
import xyz.luan.sabv.validations.Required;

public class Address {

    @Required
    private String street;
    
    @Natural
    private int number;
    
    private String complement;
    
    public Address(String street, int number) {
        this(street, number, null);
    }

    public Address(String street, int number, String complement) {
        this.street = street;
        this.number = number;
        this.complement = complement;
    }

    public String getStreet() {
        return street;
    }

    public int getNumber() {
        return number;
    }

    public String getComplement() {
        return complement;
    }
}
