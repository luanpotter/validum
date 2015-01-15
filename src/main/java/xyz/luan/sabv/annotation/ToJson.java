package xyz.luan.sabv.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import xyz.luan.sabv.ClassOutlinerNames;

public final class ToJson {

    private ToJson() {
        throw new RuntimeException("Should not be instancited");
    }

    public static String annotationToJson(Annotation a) {
        Class<?> annotationType = getAnnotationType(a);
        Stream<String> methods = Arrays.stream(annotationType.getDeclaredMethods()).map(m -> getMethodJson(a, m));
        return toMapElement("@" + annotationType.getCanonicalName(), streamToMap(methods));
    }

    public static String toMapElement(ClassOutlinerNames keyName, String valueJson) {
        return toMapElement(keyName.val(), valueJson);
    }

    public static String toMapElement(String keyString, String valueJson) {
        return "\"" + keyString + "\": " + valueJson;
    }

    private static String getMethodJson(Annotation a, Method m) {
        try {
            return toMapElement(m.getName(), annotationMethodToJson(m.invoke(a)));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> getAnnotationType(Annotation a) {
        final boolean ANNOTATIONS_MIGHT_ONLY_INHERIT_FROM_THEIR_ANNOTATION_INTERFACE = a.getClass().getInterfaces().length == 1;
        assert ANNOTATIONS_MIGHT_ONLY_INHERIT_FROM_THEIR_ANNOTATION_INTERFACE;

        Class<?> annotationType = a.getClass().getInterfaces()[0];
        return annotationType;
    }

    private static String annotationMethodToJson(Object value) {
        final List<Class<?>> NUMBERS = Arrays.asList(byte.class, short.class, int.class, long.class, float.class, double.class, Byte.class, Short.class,
                Integer.class, Long.class, Float.class, Double.class);
        if (NUMBERS.contains(value.getClass())) {
            return value.toString();
        }
        if (value.getClass().isPrimitive() || value instanceof String) {
            return strToJson(value.toString());
        }
        if (value instanceof Class<?>) {
            return strToJson(((Class<?>) value).getCanonicalName());
        }
        if (value.getClass().isEnum()) {
            return strToJson(value.getClass().getCanonicalName() + "." + value.toString());
        }
        if (value.getClass().isAnnotation()) {
            return annotationToJson((Annotation) value);
        }
        if (value.getClass().isArray()) {
            return arrayToJson(value);
        }
        throw new RuntimeException("Annotation used class type unespecified in the JVM specs: " + value.getClass());
    }

    private static String arrayToJson(Object value) {
        String elements = Arrays.stream((Object[]) value).map(obj -> annotationMethodToJson(obj)).collect(Collectors.joining(","));
        return asArray(elements);
    }

    private static String asArray(String elements) {
        return "[" + elements + "]";
    }

    public static String strToJson(String str) {
        return '"' + str + '"';
    }

    public static String streamToMap(Stream<String> stream) {
        return "{" + stream.collect(Collectors.joining(",")) + "}";
    }

    public static String typeToJson(Class<?> type) {
        String value = type.isArray() ? strToJson(ClassOutlinerNames.ARRAY_TYPE.val()) : ToJson.strToJson(type.getCanonicalName());
		return toMapElement(ClassOutlinerNames.FIELD_TYPE, value);
	}
}
