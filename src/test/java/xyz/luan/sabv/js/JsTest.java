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

    @Test @Ignore
    public void testBlah() throws NoSuchMethodException, ScriptException {
        Object ret = i.invokeFunction("runFor", "this is a test");
        System.out.println(ret);
    }
    
}
