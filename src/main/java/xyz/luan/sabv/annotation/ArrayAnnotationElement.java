package xyz.luan.sabv.annotation;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayAnnotationElement implements AnnotationElement {
    private AnnotationLevel positions;

    public ArrayAnnotationElement(Annotation... annotations) {
        this(Arrays.stream(annotations).map(ann -> new SimpleAnnotationElement(ann)).collect(Collectors.toList()));
    }

    public ArrayAnnotationElement(AnnotationElement... positions) {
        this(Arrays.asList(positions));
    }

    public ArrayAnnotationElement(List<AnnotationElement> positions) {
        this.positions = new AnnotationLevel(positions);
    }

    @Override
    public String toJson() {
        return "\"[]\" : " + positions.toJson();
    }
}