package xyz.luan.validum.outliner;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import xyz.luan.validum.BaseTest;
import xyz.luan.validum.entities.Address;
import xyz.luan.validum.entities.AdvancedPerson;
import xyz.luan.validum.entities.God;
import xyz.luan.validum.entities.Person;
import xyz.luan.validum.js.BaseJsTest;
import xyz.luan.validum.outliner.ClassOutliner;

public class ClassOutlinerTest extends BaseTest {

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
		final String expectedJson = BaseJsTest.readString(fileName);
		String result = ClassOutliner.getJson(clazz);
		try {
			JSONAssert.assertEquals(expectedJson, result, true);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
}
