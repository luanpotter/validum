package xyz.luan.sabv;

import org.junit.After;
import org.junit.Before;

import xyz.luan.sabv.customs.ValidAddress;

public abstract class BaseTestCase {

    @Before
    public void setup() {
        ValidationHelper.VALIDATORS.put(ValidAddress.class, new ValidAddress.Validator());
    }
    
    @After
    public void teardown() {
        ValidationHelper.VALIDATORS.remove(ValidAddress.class);
    }


}