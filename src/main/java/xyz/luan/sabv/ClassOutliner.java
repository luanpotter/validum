package xyz.luan.sabv;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import xyz.luan.reflection.ReflectionUtils;
import xyz.luan.sabv.annotation.AnnotationsHelper;
import xyz.luan.sabv.annotation.AnnotationsToJson;

public final class ClassOutliner {

    private ClassOutliner() {
        throw new RuntimeException("Should not be instanciated.");
    }

    public static String getJson(Class<?> c) {
        return getJson(c, ReflectionUtils.getFieldsRecursively(c));
    }

    public static String getJson(Class<?> c, List<Field> fields) {
        Stream<String> classJson = Arrays.asList("\"class\": " + getClassLevelAnnotations(c)).stream();
        Stream<String> fieldsJson = fields.stream().map(field -> "\"" + field.getName() + "\" : " + AnnotationsHelper.getNestedAnnotations(field).toJson());
        String elements = Stream.concat(classJson, fieldsJson).collect(Collectors.joining(","));
        return "{" + elements + "}";
    }

    public static String getClassLevelAnnotations(Class<?> c) {
        return "{" + getAnnotations(ValidationHelper.getValidationAnnotations(c)).collect(Collectors.joining(",")) + "}";
    }

    private static Stream<String> getAnnotations(List<Annotation> annotations) {
        return annotations.stream().map(ann -> AnnotationsToJson.annotationToJson(ann));
    }
}
