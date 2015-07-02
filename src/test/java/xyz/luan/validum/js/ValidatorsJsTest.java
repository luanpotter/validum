package xyz.luan.validum.js;

import javax.script.ScriptException;

import org.junit.Test;

public class ValidatorsJsTest extends BaseJsTest {

    @Test
    public void testRequiredInvalid() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate("{}", "xyz.luan.validum.entities.Person");
        assertErrors(errors, ":name:Required.empty");
    }

    @Test
    public void testRequiredValid() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate(map().add("name", "Karl").build(), "xyz.luan.validum.entities.Person");
        assertEmptyErrors(errors);
    }

    @Test
    public void testNumericNaturalInvalid() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate(map().add("street", "2nd Street").add("number", "-2").build(), "xyz.luan.validum.entities.Address");
        assertErrors(errors, ":number:Numeric.smallerThan{0}");
    }

    @Test
    public void testNumericNaturalValid() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate(map().add("street", "2nd Street").add("number", "2").build(), "xyz.luan.validum.entities.Address");
        assertEmptyErrors(errors);
    }

    @Test
    public void testNumericInvalid() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate(map().add("name", "Karl").add("age", "2.3").build(), "xyz.luan.validum.entities.Person");
        assertErrors(errors, ":age:Numeric.precisionGreaterThan{1}");
    }

    @Test
    public void testNumericValid() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate(map().add("name", "Karl").add("age", "23").build(), "xyz.luan.validum.entities.Person");
        assertEmptyErrors(errors);
    }

    @Test
    public void testEnumExpectValid() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate(map().add("name", "Karl").add("weakness", "HEIGHT").build(), "xyz.luan.validum.entities.AdvancedPerson");
        assertEmptyErrors(errors);
    }

    @Test
    public void testEnumExpectInvalid() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate(map().add("name", "Karl").add("weakness", "KRYPTONITE").build(), "xyz.luan.validum.entities.AdvancedPerson");
        assertErrors(errors, ":weakness:EnumExcept.in{KRYPTONITE}");
    }

    private Object runValidate(Object... args) throws ScriptException, NoSuchMethodException {
        return validum.callMember("validateJava", args);
    }
}
