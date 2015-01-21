package xyz.luan.validum;

import static xyz.luan.validum.TestCommons.assertListEquals;

import java.util.List;

import org.junit.Test;

import xyz.luan.validum.entities.Address;
import xyz.luan.validum.entities.Person;

public class ClassLevelValidationTest extends BaseTestCase {

    @Test
    public void testInvalidAddressDirectly() {
        Address address = new Address("Sesame Street", 404, "Block D");
        
        List<String> errors = validator.validate(address);
        assertListEquals(errors, ":xyz.luan.sabv.ValidAddress.invalidAddress");
    }

    @Test
    public void testInvalidAddressInsidePerson() {
        Address address = new Address("Bad Street", 200, "Block E");
        Person person = new Person(null, 14, address);
        
        List<String> errors = validator.validate(person);
        assertListEquals(errors, ":name:Required.empty", ":address:xyz.luan.sabv.ValidAddress.invalidAddress");
    }
}