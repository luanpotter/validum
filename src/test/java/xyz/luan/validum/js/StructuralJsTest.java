package xyz.luan.validum.js;

import javax.script.ScriptException;

import org.junit.Test;

public class StructuralJsTest extends BaseJsTest {

    @Test
    public void testValidation() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate("{}", "xyz.luan.validum.entities.Person");
        assertErrors(errors, ":name:Required.empty");
    }

    @Test
    public void testMultiLevelValidation() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate(map().add("address", map()).toJson(), "xyz.luan.validum.entities.Person");
        assertErrors(errors, ":address:street:Required.empty", ":name:Required.empty");
    }

    @Test
    public void testMultiLevelValidationValid() throws NoSuchMethodException, ScriptException {
        JsonMapBuilder address = map().add("street", "2nd Street");
        Object errors = runValidate(map().add("name", "Karl").add("address", address).toJson(), "xyz.luan.validum.entities.Person");
        assertEmptyErrors(errors);
    }

    @Test
    public void testClassLevelValidation() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate(map().add("street", "Bad Street").toJson(), "xyz.luan.validum.entities.Address");
        assertErrors(errors, ":ValidAddress.invalidAddress");
    }

    @Test
    public void testMultiLevelClassLevelValidation() throws NoSuchMethodException, ScriptException {
        JsonMapBuilder address = map().add("street", "Bad Street");
        Object errors = runValidate(map().add("name", "Josh").add("address", address).toJson(), "xyz.luan.validum.entities.Person");
        assertErrors(errors, ":address:ValidAddress.invalidAddress");
    }

    @Test
    public void testEnumValidation() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate(map().add("name", "Josh").add("power", "POWER_WORD").toJson(), "xyz.luan.validum.entities.AdvancedPerson");
        assertErrors(errors, ":power:InvalidEnumConstant{POWER_WORD}");
    }

    private Object runValidate(Object... args) throws ScriptException, NoSuchMethodException {
        return validum.callMember("validateJava", args);
    }
}
