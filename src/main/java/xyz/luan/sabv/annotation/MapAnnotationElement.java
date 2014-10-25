package xyz.luan.sabv.annotation;

import java.util.List;

public class MapAnnotationElement implements AnnotationElement {
    private AnnotationLevel key, value;

    public MapAnnotationElement(List<AnnotationElement> key, List<AnnotationElement> value) {
        this.key = new AnnotationLevel(key);
        this.value = new AnnotationLevel(value);
    }

    @Override
    public String toJson() {
        return "\"[k]\" : " + key.toJson() + ", \"[v]\" : " + value.toJson();
    }
}