package xyz.luan.validum.entities;

import xyz.luan.validum.customs.ValidAddress;
import xyz.luan.validum.validations.Required;
import xyz.luan.validum.validations.Numeric.Natural;

@ValidAddress
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
