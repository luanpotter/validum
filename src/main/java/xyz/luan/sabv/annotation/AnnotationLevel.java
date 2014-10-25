package xyz.luan.sabv.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AnnotationLevel extends ArrayList<AnnotationElement> {
    private static final long serialVersionUID = 8148887794656566908L;

    public AnnotationLevel(List<AnnotationElement> els) {
        super(els);
    }

    public String toJson() {
        return "{" + stream().map(p -> p.toJson()).collect(Collectors.joining(",")) + "}";
    }
}