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
        Object errors = runValidate(map().add("address", map()).build(), "xyz.luan.validum.entities.Person");
        assertErrors(errors, ":address:street:Required.empty", ":name:Required.empty");
    }

    @Test
    public void testMultiLevelValidationValid() throws NoSuchMethodException, ScriptException {
        JsonBuilder address = map().add("street", "2nd Street");
        Object errors = runValidate(map().add("name", "Karl").add("address", address).build(), "xyz.luan.validum.entities.Person");
        assertEmptyErrors(errors);
    }

    private Object runValidate(Object... args) throws ScriptException, NoSuchMethodException {
        return validum.callMember("validateJava", args);
    }
}
