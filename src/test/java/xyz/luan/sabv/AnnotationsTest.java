package xyz.luan.sabv;

import static xyz.luan.sabv.TestCommons.*;

import java.util.List;

import org.junit.Test;

import xyz.luan.sabv.entities.Address;

public class AnnotationsTest extends BaseTestCase {

    @Test
    public void testInvalidRequired() {
        Address address = new Address(null, 13);
        List<String> errors = validator.validate(address);
        assertListEquals(errors, ":street:Required.empty");
    }

    @Test
    public void testInvalidNumeric() {
        Address address = new Address("Sesame Street", -2);
        List<String> errors = validator.validate(address);
        assertListEquals(errors, ":number:Numeric.smallerThan{0.0}");
    }

    @Test
    public void testValidAddress() {
        Address address = new Address("Sesame Street", 13);
        List<String> errors = validator.validate(address);
        assertListEmpty(errors);
    }
    
}
