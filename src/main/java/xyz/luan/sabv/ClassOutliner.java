package xyz.luan.sabv;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import xyz.luan.reflection.ReflectionUtils;
import xyz.luan.sabv.annotation.AnnotationsHelper;
import xyz.luan.sabv.annotation.ToJson;

public final class ClassOutliner {

    private ClassOutliner() {
        throw new RuntimeException("Should not be instanciated.");
    }

    public static String getJson(Class<?> c) {
        return getJson(c, ReflectionUtils.getFieldsRecursively(c));
    }

    public static String getJson(Class<?> c, List<Field> fields) {
        String classElement = ClassOutlinerNames.CLASS_LEVEL_ANNOTATION.val() + ": " + getClassLevelAnnotations(c);
        Stream<String> fieldsJson = fields.stream().map(field -> "\"" + field.getName() + "\" : " + parseField(field));
        return "{" + StreamUtil.add(classElement, fieldsJson).collect(Collectors.joining(",")) + "}";
    }

    private static String parseField(Field field) {
        return AnnotationsHelper.getNestedAnnotations(field).toJson(field.getType());
    }

    public static String getClassLevelAnnotations(Class<?> c) {
        Stream<String> annotations = getAnnotations(ValidationHelper.getValidationAnnotations(c));
        String classType = ToJson.typeToJson(c);
        return "{" + StreamUtil.add(classType, annotations).collect(Collectors.joining(",")) + "}";
    }

    private static Stream<String> getAnnotations(List<Annotation> annotations) {
        return annotations.stream().map(ann -> ToJson.annotationToJson(ann));
    }
}
