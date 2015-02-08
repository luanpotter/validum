package xyz.luan.validum;

import java.util.List;

import org.junit.Test;

import xyz.luan.validum.entities.Address;

public class AnnotationsTest extends BaseTest {

    @Test
    public void testInvalidRequired() {
        Address address = new Address(null, 13);
        List<String> errors = validator.validate(address);
        assertListEquals(errors, ":street:" + ErrorMessagesReference.REQUIRED);
    }

    @Test
    public void testInvalidNumeric() {
        Address address = new Address("Sesame Street", -2);
        List<String> errors = validator.validate(address);
        assertListEquals(errors, ":number:" + ErrorMessagesReference.SMALLER_THAN + "{0.0}");
    }

    @Test
    public void testValidAddress() {
        Address address = new Address("Sesame Street", 13);
        List<String> errors = validator.validate(address);
        assertListEmpty(errors);
    }
}
