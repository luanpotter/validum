package xyz.luan.validum.js;

import javax.script.ScriptException;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import org.junit.Before;
import org.junit.Test;

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

    private Object runValidate(Object... args) throws ScriptException, NoSuchMethodException {
        return validum.callMember("validateJava", args);
    }
}
