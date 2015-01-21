package xyz.luan.sabv;

import org.junit.After;
import org.junit.Before;

import xyz.luan.sabv.customs.ValidAddress;

public abstract class BaseTestCase {

	protected Validator validator;

	@Before
	public void setup() {
		validator = Validator.withDefaults().addValidation(ValidAddress.class, new ValidAddress.Validator());
	}

}