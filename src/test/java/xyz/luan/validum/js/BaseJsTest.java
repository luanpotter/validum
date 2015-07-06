package xyz.luan.validum.js;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.ECMAException;
import junit.framework.Assert;

import org.junit.Before;

public class BaseJsTest {

    protected ScriptObjectMirror validum;

    @Before
    public void setup() throws NoSuchMethodException, ScriptException {
        validum = (ScriptObjectMirror) BaseJsTest.setupInvocable().invokeFunction("eval", "validum");
    }

    public static Invocable setupInvocable() throws ScriptException, NoSuchMethodException {
        final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        final Invocable invocable = (Invocable) engine;

        engine.eval(reader("dependencies.js"));
        engine.eval(reader("common.js"));
        engine.eval(reader("converter.js"));
        engine.eval(reader("validators.js"));
        engine.eval(reader("validum.js"));

        engine.eval(reader("validum_java_binder.js"));
        engine.eval(reader("custom_validators.js"));

        setupClasses(engine, new String[] { "address", "person", "advanced_person", "god", "power", "weakness" });

        return invocable;
    }

    public interface JsonElement {
        public String toJson();
    }

    public static class JsonString implements JsonElement {
        private String str;

        public JsonString(String str) {
            this.str = str;
        }

        @Override
        public String toJson() {
            return '"' + str + '"';
        }
    }

    public static class JsonArrayBuilder implements JsonElement {
        private List<JsonElement> array;

        public JsonArrayBuilder() {
            this.array = new ArrayList<>();
        }

        public JsonArrayBuilder add(String value) {
            return add(new JsonString(value));
        }

        public JsonArrayBuilder add(JsonElement value) {
            this.array.add(value);
            return this;
        }

        @Override
        public String toJson() {
            return "[" + array.stream().map(e -> e.toJson()).collect(Collectors.joining(", ")) + "]";
        }
    }

    public static class JsonMapBuilder implements JsonElement {
        private Map<String, JsonElement> map;

        public JsonMapBuilder() {
            this.map = new HashMap<>();
        }

        public JsonMapBuilder add(String key, String value) {
            return add(key, new JsonString(value));
        }

        public JsonMapBuilder add(String key, JsonElement obj) {
            this.map.put(key, obj);
            return this;
        }

        @Override
        public String toJson() {
            Stream<String> toMap = map.keySet().stream().map(k -> '"' + k + "\": " + map.get(k).toJson());
            return "{" + toMap.collect(Collectors.joining(", ")) + " }";
        }
    }

    public static JsonMapBuilder map() {
        return new JsonMapBuilder();
    }

    public static JsonArrayBuilder array() {
        return new JsonArrayBuilder();
    }

    public static String deepToString(Object object) {
        if (object instanceof ScriptObjectMirror) {
            ScriptObjectMirror map = ((ScriptObjectMirror) object);
            if (map.isArray()) {
                return "[ " + map.keySet().stream().map((key) -> deepToString(map.get(key))).collect(Collectors.joining(", ")) + " ]";
            } else {
                return "{ " + map.keySet().stream().map((key) -> key + ": " + deepToString(map.get(key))).collect(Collectors.joining(", ")) + " }";
            }
        }
        return object == null ? "null" : object.toString();
    }

    private static void setupClasses(ScriptEngine engine, String[] names) throws ScriptException {
        for (String name : names) {
            engine.eval("validum.convert.setup(" + readString(name + ".json") + ");");
        }
    }

    public static String readString(String fileName) {
        StringBuilder text = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(reader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (EOFException ex) {
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return text.toString();
    }

    private static FileReader reader(String fileName) {
        try {
            return new FileReader(BaseJsTest.class.getResource("/" + fileName).getFile());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected void assertErrors(Object errorsObj, String... expectedArray) {
        List<String> errors = new ArrayList<>();
        for (Object error : ((ScriptObjectMirror) errorsObj).values()) {
            errors.add(error.toString());
        }
        List<String> expected = Arrays.asList(expectedArray);

        Collections.sort(errors);
        Collections.sort(expected);

        Assert.assertEquals(expected, errors);
    }

    protected void assertEmptyErrors(Object errorsObj) {
        Assert.assertEquals(0, ((ScriptObjectMirror) errorsObj).values().size());
    }

    protected void assertFailure(ECMAException ex, final String message) {
        ScriptObjectMirror converterException = (ScriptObjectMirror) ex.getEcmaError();
        Assert.assertEquals(message, converterException.callMember("getMessage"));
    }
}
