package xyz.luan.sabv.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import xyz.luan.sabv.StreamUtil;

public class AnnotationLevel extends ArrayList<AnnotationElement> {
    private static final long serialVersionUID = 8148887794656566908L;

    public AnnotationLevel(List<AnnotationElement> els) {
        super(els);
    }

    public String toJson(Class<?> type) {
        return "{" + createStream(type).collect(Collectors.joining(",")) + "}";
    }

    private Stream<String> createStream(Class<?> clazz) {
        return StreamUtil.add(ToJson.typeToJson(clazz), stream().map(p -> p.toJson()));
    }
}