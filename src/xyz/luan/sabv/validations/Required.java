package xyz.luan.sabv.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import xyz.luan.sabv.Validation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Validation(Object.class)
public @interface Required {

}
