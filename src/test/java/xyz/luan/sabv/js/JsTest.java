package xyz.luan.sabv.js;

import javax.script.Invocable;
import javax.script.ScriptException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class JsTest {

    private Invocable i;

    @Before
    public void setup() throws NoSuchMethodException, ScriptException {
        i = JsSetup.setupInvocable();
    }

    @Ignore
    @Test
    public void testBlah() throws NoSuchMethodException, ScriptException {
        Object ret = runValidate("'my string'");
        System.out.println(ret);
    }

    private Object runValidate(String arg) throws ScriptException, NoSuchMethodException {
        return i.invokeFunction("eval", "sabv.validate(" + arg + ")");
    }

}
