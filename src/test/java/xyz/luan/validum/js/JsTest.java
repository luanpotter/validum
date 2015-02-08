package xyz.luan.validum.js;

import javax.script.ScriptException;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import org.junit.Before;
import org.junit.Test;

import xyz.luan.validum.js.JsSetup.JsonBuilder;

public class JsTest {

    private ScriptObjectMirror validum;

    @Before
    public void setup() throws NoSuchMethodException, ScriptException {
        validum = (ScriptObjectMirror) JsSetup.setupInvocable().invokeFunction("eval", "validum");
    }

    @Test
    public void testValidation() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate("{}", "xyz.luan.validum.entities.Person");
        JsSetup.assertErrors(errors, ":name:Required.empty");
    }

    @Test
    public void testMultiLevelValidation() throws NoSuchMethodException, ScriptException {
        Object errors = runValidate(JsSetup.map().add("address", JsSetup.map()).build(), "xyz.luan.validum.entities.Person");
        JsSetup.assertErrors(errors, ":address:street:Required.empty", ":name:Required.empty");
    }

    @Test
    public void testMultiLevelValidationValid() throws NoSuchMethodException, ScriptException {
        JsonBuilder address = JsSetup.map().add("street", "2nd Street");
        Object errors = runValidate(JsSetup.map().add("name", "Karl").add("address", address).build(), "xyz.luan.validum.entities.Person");
        JsSetup.assertEmptyErrors(errors);
    }

    private Object runValidate(Object... args) throws ScriptException, NoSuchMethodException {
        return validum.callMember("validateJava", args);
    }
}
