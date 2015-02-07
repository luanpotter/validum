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

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import junit.framework.Assert;

public final class JsSetup {

    private JsSetup() {
        throw new RuntimeException("This class should not be instanciated.");
    }

    public static Invocable setupInvocable() throws ScriptException, NoSuchMethodException {
        final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        final Invocable invocable = (Invocable) engine;

        engine.eval(reader("big.js"));
        engine.eval(reader("common.js"));
        engine.eval(reader("validum_java_binder.js"));
        engine.eval(reader("converter.js"));
        engine.eval(reader("validators.js"));
        engine.eval(reader("validum.js"));
        setupClasses(engine, new String[] { "address", "person", "advanced_person", "god" });

        return invocable;
    }

    public static class JsonBuilder {
        private Map<String, String> map;

        public JsonBuilder() {
            this.map = new HashMap<>();
        }

        public JsonBuilder add(String key, String value) {
            this.map.put(key, value);
            return this;
        }

        private static String wrap(String str) {
            return '"' + str + '"';
        }

        @SuppressWarnings("unchecked")
        public static String mapToString(Map<String, String> map) {
            return "{" + map.keySet().stream().map((k) -> {
                Object element = map.get(k);
                String elementString = element instanceof Map ? mapToString((Map<String, String>) element) : wrap(element.toString());
                return '"' + k + "\": " + elementString;
            }).collect(Collectors.joining(", ")) + " }";
        }

        public String build() {
            return mapToString(this.map);
        }
    }

    public static JsonBuilder map() {
        return new JsonBuilder();
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
            return new FileReader(JsSetup.class.getResource("/" + fileName).getFile());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void assertErrors(Object errorsObj, String... expectedArray) {
        List<String> errors = new ArrayList<>();
        for (Object error : ((ScriptObjectMirror) errorsObj).values()) {
            errors.add(error.toString());
        }
        List<String> expected = Arrays.asList(expectedArray);

        Collections.sort(errors);
        Collections.sort(expected);

        Assert.assertEquals(expected, errors);
    }
}
