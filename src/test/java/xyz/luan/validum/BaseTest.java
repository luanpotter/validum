package xyz.luan.validum;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;

import xyz.luan.validum.customs.ValidAddress;

public abstract class BaseTest {

    protected Validator validator;

    @Before
    public void setup() {
        validator = Validator.withDefaults().addValidation(ValidAddress.class, new ValidAddress.Validator());
    }

    protected void assertListEmpty(List<String> actualList) {
        assertListEquals(actualList);
    }

    protected void assertListEquals(List<String> actualList, String... expected) {
        String[] actual = actualList.toArray(new String[actualList.size()]);

        Arrays.sort(expected);
        Arrays.sort(actual);

        Assert.assertArrayEquals(expected, actual);
    }
}