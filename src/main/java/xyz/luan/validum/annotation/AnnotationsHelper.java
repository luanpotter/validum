package xyz.luan.validum.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import xyz.luan.reflection.ReflectionUtils;

public final class AnnotationsHelper {

    private AnnotationsHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static AnnotationLevel getNestedAnnotations(Field f) {
        List<AnnotationElement> result = Arrays.stream(f.getAnnotations()).map(ann -> new SimpleAnnotationElement(ann))
                .collect(Collectors.toList());
        addAnnotationsAtCurrentLevel(result, f.getGenericType(), f.getAnnotatedType());
        return new AnnotationLevel(result);
    }

    private static void addAnnotationsAtCurrentLevel(List<AnnotationElement> list, Type type, AnnotatedType at) {
        Class<?> clazz = ReflectionUtils.getClass(type);
        if (clazz.isArray()) {
            AnnotatedType childAnnotatedType = ((AnnotatedArrayType) at).getAnnotatedGenericComponentType();
            Class<?> childType = clazz.getComponentType();
            list.add(getArrayAnnotatedElement(at, childType, childAnnotatedType));
        } else if (Collection.class.isAssignableFrom(clazz)) {
            AnnotatedType childAnnotatedType = ((AnnotatedParameterizedType) at).getAnnotatedActualTypeArguments()[0];
            Type childType = ((ParameterizedType) type).getActualTypeArguments()[0];
            list.add(getListAnnotatedElement(childType, childAnnotatedType));
        } else if (Map.class.isAssignableFrom(clazz)) {
            AnnotatedType[] att = ((AnnotatedParameterizedType) at).getAnnotatedActualTypeArguments();
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            list.add(getMapAnnotationElement(att, types));
        }
    }

    private static AnnotationElement getArrayAnnotatedElement(AnnotatedType at, Type childType, AnnotatedType childAnnotatedType) {
        List<AnnotationElement> list = toAnnotationElementList(at.getAnnotations());
        addAnnotationsAtCurrentLevel(list, childType, childAnnotatedType);
        return new ArrayAnnotationElement(childType, list);
    }

    private static AnnotationElement getListAnnotatedElement(Type childType, AnnotatedType childAnnotatedType) {
        List<AnnotationElement> list = toAnnotationElementList(childType, childAnnotatedType);
        return new ArrayAnnotationElement(childType, list);
    }

    private static AnnotationElement getMapAnnotationElement(AnnotatedType[] att, Type[] types) {
        List<AnnotationElement> keyList = toAnnotationElementList(types[0], att[0]);
        List<AnnotationElement> valueList = toAnnotationElementList(types[1], att[1]);

        return new MapAnnotationElement(types[0], keyList, types[1], valueList);
    }

    private static List<AnnotationElement> toAnnotationElementList(Type childType, AnnotatedType childAnnotatedType) {
        List<AnnotationElement> list = toAnnotationElementList(childAnnotatedType.getAnnotations());
        addAnnotationsAtCurrentLevel(list, childType, childAnnotatedType);
        return list;
    }

    private static List<AnnotationElement> toAnnotationElementList(Annotation[] anns) {
        return Arrays.stream(anns).map(ann -> new SimpleAnnotationElement(ann)).collect(Collectors.toList());
    }
}
