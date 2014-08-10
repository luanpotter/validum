package xyz.luan.sabv;

import static xyz.luan.sabv.TestCommons.assertListEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import xyz.luan.sabv.customs.ValidAddress;
import xyz.luan.sabv.entities.Address;
import xyz.luan.sabv.entities.Person;

public class ClassLevelValidationTest {
    @Before
    public void setup() {
        ValidationHelper.VALIDATORS.put(ValidAddress.class, new ValidAddress.Validator());
    }

    @Test
    public void testInvalidAddressDirectly() {
        Address address = new Address("Sesame Street", 404, "Block D");
        
        List<String> errors = ValidationHelper.validate(address);
        assertListEquals(errors, ":xyz.luan.sabv.ValidAddress.invalidAddress");
    }

    @Test
    public void testInvalidAddressInsidePerson() {
        Address address = new Address("Bad Street", 200, "Block E");
        Person person = new Person(null, 14, address);
        
        List<String> errors = ValidationHelper.validate(person);
        assertListEquals(errors, ":name:Required.empty", ":address:xyz.luan.sabv.ValidAddress.invalidAddress");
    }
}