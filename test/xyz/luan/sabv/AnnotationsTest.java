package xyz.luan.sabv;

import static xyz.luan.sabv.TestCommons.*;

import java.util.List;

import org.junit.Test;

import xyz.luan.sabv.entities.Address;

public class AnnotationsTest {

    @Test
    public void testInvalidRequired() {
        Address address = new Address(null, 13);
        List<String> errors = ValidationHelper.validate(address);
        assertListEquals(errors, ":street:Required.empty");
    }

    @Test
    public void testInvalidNumeric() {
        Address address = new Address("Sesame Street", -2);
        List<String> errors = ValidationHelper.validate(address);
        assertListEquals(errors, ":number:Numeric.smallerThan{0}");
    }
    
}
