package xyz.luan.validum.js;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public final class JsSetup {

    private JsSetup() {
        throw new RuntimeException("This class should not be instanciated.");
    }

    public static Invocable setupInvocable() throws ScriptException, NoSuchMethodException {
        final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        final Invocable invocable = (Invocable) engine;

        engine.eval(reader("big.js"));
        engine.eval(reader("converter.js"));
        engine.eval(reader("validators.js"));
        engine.eval(reader("validum.js"));
        evalClassDefs(engine, new String[] { "address", "person", "advanced_person", "god" });

        return invocable;
    }

    public static String deepToString(Object object) {
        if (object instanceof ScriptObjectMirror) {
            ScriptObjectMirror map = ((ScriptObjectMirror) object);
            String elements = map.keySet().stream().map((key) -> key + ": " + deepToString(map.get(key))).collect(Collectors.joining(","));
            return "{" + elements + "}";
        }
        return object.toString();
    }

    private static void evalClassDefs(ScriptEngine engine, String[] names) throws ScriptException {
        engine.eval("classDefs = {};");
        for (String name : names) {
            engine.eval("classDefs['" + name + "'] = " + readString(name + ".json") + ";");
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
}
