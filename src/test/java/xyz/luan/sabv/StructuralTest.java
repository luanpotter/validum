package xyz.luan.sabv;

import static xyz.luan.sabv.TestCommons.assertListEmpty;
import static xyz.luan.sabv.TestCommons.assertListEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import xyz.luan.sabv.customs.ValidAddress;
import xyz.luan.sabv.entities.Address;
import xyz.luan.sabv.entities.AdvancedPerson;
import xyz.luan.sabv.entities.Person;
import xyz.luan.sabv.entities.Power;
import xyz.luan.sabv.entities.Weakness;

public class StructuralTest {

    @Before
    public void setup() {
        ValidationHelper.VALIDATORS.put(ValidAddress.class, new ValidAddress.Validator());
    }

    @Test
    public void testValidAdvancedPersonWithValidAddress() {
        Address address = new Address("Sesame Street", 42, "Block D");
        Person person = new AdvancedPerson("George", 21, address, Power.FLIGHT, Weakness.CATS);
        
        List<String> errors = ValidationHelper.validate(person);
        assertListEmpty(errors);
    }
    
    @Test
    public void testInvalidAdvancedPersonWithValidAddress() {
        Address address = new Address("Sesame Street", 42, "Block D");
        Person person = new AdvancedPerson(null, 21, address, Power.IMMORTALITY, Weakness.CATS);
        
        List<String> errors = ValidationHelper.validate(person);
        assertListEquals(errors, ":power:EnumOnly.notIn{strength,flight,telekinesis}", ":name:Required.empty");
    }
    
    @Test
    public void testValidAdvancedPersonWithInalidAddress() {
        Address address = new Address(null, 42, "Block E");
        Person person = new AdvancedPerson("Flying Rat Man", 21, address, Power.FLIGHT, Weakness.CATS);
        
        List<String> errors = ValidationHelper.validate(person);
        assertListEquals(errors, ":address:street:Required.empty");
    }
    
    @Test
    public void testInvalidAdvancedPersonWithInalidAddress() {
        Address address = new Address("Baker Street", -221, "Apartment B");
        Person person = new AdvancedPerson("Flying Rat Man", 21, address, Power.FLIGHT, Weakness.KRIPTONITE);
        
        List<String> errors = ValidationHelper.validate(person);
        assertListEquals(errors, ":address:number:Numeric.smallerThan{0.0}", ":weakness:EnumExcept.was{kriptonite}");
    }
    
    @Test
    public void testValidPersonWithValidAddress() {
        Address address = new Address("Sesame Street", 42, "Block D");
        Person person = new Person("George", 21, address);
        
        List<String> errors = ValidationHelper.validate(person);
        assertListEmpty(errors);
    }
    
    @Test
    public void testInalidPersonWithInvalidAddress() {
        Address address = new Address(null, 4, "Block D");
        Person person = new Person(null, 21, address);
        
        List<String> errors = ValidationHelper.validate(person);
        Assert.assertEquals(2, errors.size());
        assertListEquals(errors, ":address:street:Required.empty", ":name:Required.empty");
    }

    @Test
    public void testValidAddress() {
        Address address = new Address("Sesame Street", 42);
        List<String> errors = ValidationHelper.validate(address);
        assertListEmpty(errors);
    }

    @Test
    public void testInvalidAddress() {
        Address address = new Address(null, 13);
        List<String> errors = ValidationHelper.validate(address);
        assertListEquals(errors, ":street:Required.empty");
    }
}
