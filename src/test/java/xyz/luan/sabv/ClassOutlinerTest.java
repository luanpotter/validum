package xyz.luan.sabv;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import xyz.luan.sabv.entities.Address;
import xyz.luan.sabv.entities.AdvancedPerson;
import xyz.luan.sabv.entities.God;
import xyz.luan.sabv.entities.Person;
import xyz.luan.sabv.js.JsSetup;

public class ClassOutlinerTest extends BaseTestCase {

	@Test
	public void testAddressOutline() {
		testOutline(Address.class, "address.json");
	}

	@Test
	public void testPersonOutline() {
		testOutline(Person.class, "person.json");
	}

	@Test
	public void testAdvancedPersonOutline() {
		testOutline(AdvancedPerson.class, "advanced_person.json");
	}

	@Test
	public void testGodOutline() {
		testOutline(God.class, "god.json");
	}

	private void testOutline(Class<?> clazz, String fileName) {
		final String expectedJson = JsSetup.readString(fileName);
		String result = ClassOutliner.getJson(clazz);
		try {
			JSONAssert.assertEquals(expectedJson, result, true);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
}
