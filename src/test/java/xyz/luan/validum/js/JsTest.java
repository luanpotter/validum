package xyz.luan.validum.js;

import javax.script.Invocable;
import javax.script.ScriptException;

import junit.framework.Assert;

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
        Object ret = runValidate("'my string', classDefs['person']");
        System.out.println(JsSetup.deepToString(ret));
        Assert.fail();
    }

    private Object runValidate(String arg) throws ScriptException, NoSuchMethodException {
        return i.invokeFunction("eval", "validum.validate(" + arg + ")");
    }

}
