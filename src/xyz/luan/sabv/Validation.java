package xyz.luan.sabv;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Validation {
    Class<?>[] value();
    
    boolean requireValidFields() default false;
}
