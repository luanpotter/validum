package xyz.luan.validum;

import org.junit.After;
import org.junit.Before;

import xyz.luan.validum.Validator;
import xyz.luan.validum.customs.ValidAddress;

public abstract class BaseTestCase {

	protected Validator validator;

	@Before
	public void setup() {
		validator = Validator.withDefaults().addValidation(ValidAddress.class, new ValidAddress.Validator());
	}

}