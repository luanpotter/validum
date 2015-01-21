package xyz.luan.sabv;

import java.lang.annotation.Annotation;
import java.util.List;

public interface AnnotationValidator<T, A extends Annotation> {
    public List<String> validate(T object, A annotation);
}
