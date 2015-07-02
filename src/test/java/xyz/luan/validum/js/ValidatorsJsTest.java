package xyz.luan.validum.js;

import javax.script.ScriptException;

import org.junit.Test;

import xyz.luan.validum.entities.Address;
import xyz.luan.validum.entities.AdvancedPerson;
import xyz.luan.validum.entities.Person;

public class ValidatorsJsTest extends BaseJsTest {

    @Test
    public void testRequiredInvalid() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate(map(), Person.class);
        assertErrors(errors, ":name:Required.empty");
    }

    @Test
    public void testRequiredValid() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate(map().add("name", "Karl"), Person.class);
        assertEmptyErrors(errors);
    }

    @Test
    public void testNumericNaturalInvalid() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate(map().add("street", "2nd Street").add("number", "-2"), Address.class);
        assertErrors(errors, ":number:Numeric.smallerThan{0}");
    }

    @Test
    public void testNumericNaturalValid() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate(map().add("street", "2nd Street").add("number", "2"), Address.class);
        assertEmptyErrors(errors);
    }

    @Test
    public void testNumericInvalid() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate(map().add("name", "Karl").add("age", "2.3"), Person.class);
        assertErrors(errors, ":age:Numeric.precisionGreaterThan{1}");
    }

    @Test
    public void testNumericValid() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate(map().add("name", "Karl").add("age", "23"), Person.class);
        assertEmptyErrors(errors);
    }

    @Test
    public void testEnumExpectValid() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate(map().add("name", "Karl").add("weakness", "HEIGHT"), AdvancedPerson.class);
        assertEmptyErrors(errors);
    }

    @Test
    public void testEnumExpectInvalid() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate(map().add("name", "Karl").add("weakness", "KRYPTONITE"), AdvancedPerson.class);
        assertErrors(errors, ":weakness:EnumExcept.in{KRYPTONITE}");
    }

    @Test
    public void testArrayValid() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate(map().add("name", "Karl").add("weakness", "KRYPTONITE"), AdvancedPerson.class);
        assertErrors(errors, ":weakness:EnumExcept.in{KRYPTONITE}");
    }

    private Object runValidate(JsonElement el, Class<?> clazz) throws ScriptException, NoSuchMethodException {
        return validum.callMember("validateJava", el.toJson(), clazz.getName());
    }
}
