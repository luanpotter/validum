package xyz.luan.validum.js;

import java.math.BigDecimal;

import javax.script.ScriptException;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.ECMAException;
import junit.framework.Assert;

import org.junit.Test;

public class ConverterTest extends BaseJsTest {

    @Test
    public void testBigDecimalConvertion() throws NoSuchMethodException, ScriptException {
        final String integer = "42389247892374892378423789.234982389423783798248723";
        Object ret = runConverter(integer, BigDecimal.class);
        assertBigEquals(integer, ret);
    }

    @Test
    public void testIntegerConvertion() throws NoSuchMethodException, ScriptException {
        final String integer = "42";
        Object ret = runConverter(integer, Integer.class);
        assertBigEquals(integer, ret);
    }

    private void assertBigEquals(final String integer, Object ret) {
        Assert.assertEquals(ScriptObjectMirror.class, ret.getClass());
        Assert.assertEquals(integer, ((ScriptObjectMirror) ret).callMember("toFixed"));
    }

    @Test
    public void testConvertionTooDecimalPlaces() throws NoSuchMethodException, ScriptException {
        try {
            final String integer = "655.36";
            runConverter(integer, Short.class);
        } catch (ECMAException ex) {
            assertFailure(ex, "Numeric.precisionGreaterThan{1}");
            return;
        }
        Assert.fail("Should have thrown ConverterException");
    }

    @Test
    public void testConvertionWithNaN() throws NoSuchMethodException, ScriptException {
        try {
            final String integer = "655a36";
            runConverter(integer, Short.class);
        } catch (ECMAException ex) {
            assertFailure(ex, "Numeric.notANumber");
            return;
        }
        Assert.fail("Should have thrown ConverterException");
    }

    @Test
    public void testConvertionWithOverflow() throws NoSuchMethodException, ScriptException {
        try {
            final String integer = "65536";
            runConverter(integer, Short.class);
        } catch (ECMAException ex) {
            assertFailure(ex, "Numeric.greaterThan{32767}");
            return;
        }
        Assert.fail("Should have thrown ConverterException");
    }

    @Test
    public void testStringConvertion() throws NoSuchMethodException, ScriptException {
        final String text = "my string";
        Object ret = runConverter(text, String.class);
        Assert.assertEquals(String.class, ret.getClass());
        Assert.assertEquals("my string", ret);
    }

    private Object runConverter(String obj, Class<?> type) throws ECMAException, NoSuchMethodException {
        return validum.callMember("convert", obj, type.getCanonicalName());
    }

}
