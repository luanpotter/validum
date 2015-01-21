package xyz.luan.validum;

import java.lang.annotation.Annotation;
import java.util.List;

public interface AnnotationValidator<T, A extends Annotation> {
    public List<String> validate(T object, A annotation);
}
