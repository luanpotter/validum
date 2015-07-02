package xyz.luan.validum;

import static xyz.luan.validum.ErrorMessagesReference.ENUM_ONLY;
import static xyz.luan.validum.ErrorMessagesReference.ENUM_EXCEPT;
import static xyz.luan.validum.ErrorMessagesReference.REQUIRED;
import static xyz.luan.validum.ErrorMessagesReference.SMALLER_THAN;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import xyz.luan.validum.entities.Address;
import xyz.luan.validum.entities.AdvancedPerson;
import xyz.luan.validum.entities.Person;
import xyz.luan.validum.entities.Power;
import xyz.luan.validum.entities.Weakness;

public class StructuralTest extends BaseTest {

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
        assertListEquals(errors, ":power:" + ENUM_ONLY + "{STRENGTH,FLIGHT,TELEKINESIS}", ":name:" + REQUIRED);
    }

    @Test
    public void testValidAdvancedPersonWithInalidAddress() {
        Address address = new Address(null, 42, "Block E");
        Person person = new AdvancedPerson("Flying Rat Man", 21, address, Power.FLIGHT, Weakness.CATS);

        List<String> errors = validator.validate(person);
        assertListEquals(errors, ":address:street:" + REQUIRED);
    }

    @Test
    public void testInvalidAdvancedPersonWithInalidAddress() {
        Address address = new Address("Baker Street", -221, "Apartment B");
        Person person = new AdvancedPerson("Flying Rat Man", 21, address, Power.FLIGHT, Weakness.KRYPTONITE);

        List<String> errors = validator.validate(person);
        assertListEquals(errors, ":address:number:" + SMALLER_THAN + "{0.0}", ":weakness:" + ENUM_EXCEPT + "{KRYPTONITE}");
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
        assertListEquals(errors, ":address:street:" + REQUIRED, ":name:" + REQUIRED);
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
        assertListEquals(errors, ":street:" + REQUIRED);
    }
}
