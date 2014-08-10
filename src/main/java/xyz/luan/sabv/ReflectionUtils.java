package xyz.luan.sabv;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class ReflectionUtils {

    private ReflectionUtils() {
        throw new RuntimeException("Should not be instanciated");
    }

    public static boolean isInnerClass(Class<?> clazz) {
        return clazz.getEnclosingClass() != null && !Modifier.isStatic(clazz.getModifiers());
    }

    public static List<Field> getImmediateFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields()).filter(field -> !Modifier.isStatic(field.getModifiers()) && !field.isSynthetic()).collect(Collectors.toList());
    }

    public static List<Field> getFieldsRecursively(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (!isJavaClass(clazz)) {
            fields.addAll(ReflectionUtils.getImmediateFields(clazz));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    public static boolean isJavaClass(Class<?> clazz) {
        return Object.class.equals(clazz) || clazz.isPrimitive() || clazz.isEnum() || clazz.isArray() || clazz.getPackage().getName().startsWith("java.") || clazz.getPackage().getName().startsWith("javax.");
    }
}
