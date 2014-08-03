package xyz.luan.sabv;

import java.lang.annotation.Annotation;

public interface Validator<T, A extends Annotation> {
    
    public String validate(T object, A annotation);
}
