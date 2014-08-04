package xyz.luan.sabv;

import java.lang.annotation.Annotation;
import java.util.List;

public interface Validator<T, A extends Annotation> {
    
    public List<String> validate(T object, A annotation);
}
