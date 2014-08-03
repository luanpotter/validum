package xyz.luan.sabv;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import xyz.luan.sabv.entities.Address;
import xyz.luan.sabv.entities.Person;

public class SimpleTest {
    
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
        errors.sort(String.CASE_INSENSITIVE_ORDER);
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

    private void assertListEmpty(List<String> actualList) {
        assertListEquals(actualList);
    }
    
    private void assertListEquals(List<String> actualList, String... expected) {
        Assert.assertEquals(actualList.size(), expected.length);
        if (expected.length == 0) {
            return;
        }

        String[] actual = actualList.toArray(new String [actualList.size()]);
        
        Arrays.sort(expected);
        Arrays.sort(actual);
        
        Assert.assertArrayEquals(expected, actual);
    }
}
