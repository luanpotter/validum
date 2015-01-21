package xyz.luan.validum.annotation;

import java.lang.reflect.Type;
import java.util.List;

import xyz.luan.reflection.ReflectionUtils;
import xyz.luan.validum.outliner.ClassOutlinerNames;

public class ArrayAnnotationElement implements AnnotationElement {

    private Class<?> componentType;
    private AnnotationLevel positions;

    public ArrayAnnotationElement(Type componentType, List<AnnotationElement> positions) {
        this.componentType = ReflectionUtils.getClass(componentType);
        this.positions = new AnnotationLevel(positions);
    }

    @Override
    public String toJson() {
        return ClassOutlinerNames.ARRAY_ELEMENT.val() + " : " + positions.toJson(componentType);
    }
}