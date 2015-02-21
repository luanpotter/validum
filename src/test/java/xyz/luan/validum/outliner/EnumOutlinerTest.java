package xyz.luan.validum.outliner;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import xyz.luan.validum.entities.Power;
import xyz.luan.validum.entities.Weakness;
import xyz.luan.validum.js.BaseJsTest;

public class EnumOutlinerTest {

	@Test
	public void testEnumOutliner() {
		testOutline(Power.class, "power.json");
		testOutline(Weakness.class, "weakness.json");
	}

	private void testOutline(Class<? extends Enum<?>> clazz, String fileName) {
		final String expectedJson = BaseJsTest.readString(fileName);
		String result = EnumOutliner.getJson(clazz);
		try {
			JSONAssert.assertEquals(expectedJson, result, true);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
}
