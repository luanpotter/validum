package xyz.luan.validum;

import static xyz.luan.validum.TestCommons.assertListEmpty;
import static xyz.luan.validum.TestCommons.assertListEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import xyz.luan.validum.entities.Address;
import xyz.luan.validum.entities.AdvancedPerson;
import xyz.luan.validum.entities.Person;
import xyz.luan.validum.entities.Power;
import xyz.luan.validum.entities.Weakness;

public class StructuralTest extends BaseTestCase {

    @Test
    public void testValidAdvancedPersonWithValidAddress() {
        Address address = new Address("Sesame Street", 42, "Block D");
        Person person = new AdvancedPerson("George", 21, address, Power.FLIGHT, Weakness.CATS);
        
        List<String> errors = validator.validate(person);
        assertListEmpty(errors);
    }
    
    @Test
    public void testInvalidAdvancedPersonWithValidAddress() {
        Address address = new Address("Sesame Street", 42, "Block D");
        Person person = new AdvancedPerson(null, 21, address, Power.IMMORTALITY, Weakness.CATS);
        
        List<String> errors = validator.validate(person);
        assertListEquals(errors, ":power:EnumOnly.notIn{strength,flight,telekinesis}", ":name:Required.empty");
    }
    
    @Test
    public void testValidAdvancedPersonWithInalidAddress() {
        Address address = new Address(null, 42, "Block E");
        Person person = new AdvancedPerson("Flying Rat Man", 21, address, Power.FLIGHT, Weakness.CATS);
        
        List<String> errors = validator.validate(person);
        assertListEquals(errors, ":address:street:Required.empty");
    }
    
    @Test
    public void testInvalidAdvancedPersonWithInalidAddress() {
        Address address = new Address("Baker Street", -221, "Apartment B");
        Person person = new AdvancedPerson("Flying Rat Man", 21, address, Power.FLIGHT, Weakness.KRIPTONITE);
        
        List<String> errors = validator.validate(person);
        assertListEquals(errors, ":address:number:Numeric.smallerThan{0.0}", ":weakness:EnumExcept.was{kriptonite}");
    }
    
    @Test
    public void testValidPersonWithValidAddress() {
        Address address = new Address("Sesame Street", 42, "Block D");
        Person person = new Person("George", 21, address);
        
        List<String> errors = validator.validate(person);
        assertListEmpty(errors);
    }
    
    @Test
    public void testInalidPersonWithInvalidAddress() {
        Address address = new Address(null, 4, "Block D");
        Person person = new Person(null, 21, address);
        
        List<String> errors = validator.validate(person);
        Assert.assertEquals(2, errors.size());
        assertListEquals(errors, ":address:street:Required.empty", ":name:Required.empty");
    }

    @Test
    public void testValidAddress() {
        Address address = new Address("Sesame Street", 42);
        List<String> errors = validator.validate(address);
        assertListEmpty(errors);
    }

    @Test
    public void testInvalidAddress() {
        Address address = new Address(null, 13);
        List<String> errors = validator.validate(address);
        assertListEquals(errors, ":street:Required.empty");
    }
}
