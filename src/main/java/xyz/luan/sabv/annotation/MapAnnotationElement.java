package xyz.luan.sabv.annotation;

import java.lang.reflect.Type;
import java.util.List;

import xyz.luan.reflection.ReflectionUtils;
import xyz.luan.sabv.ClassOutlinerNames;

public class MapAnnotationElement implements AnnotationElement {

    private Class<?> keyType, valueType;
    private AnnotationLevel key, value;

    public MapAnnotationElement(Type keyType, List<AnnotationElement> key, Type valueType, List<AnnotationElement> value) {
        this.keyType = ReflectionUtils.getClass(keyType);
        this.key = new AnnotationLevel(key);

        this.valueType = ReflectionUtils.getClass(valueType);
        this.value = new AnnotationLevel(value);
    }

    @Override
    public String toJson() {
        String keyJson = ClassOutlinerNames.MAP_KEY.val() + " : " + key.toJson(keyType);
        String valueJson = ClassOutlinerNames.MAP_VALUE.val() + " : " + value.toJson(valueType);
        return keyJson + ", " + valueJson;
    }
}