package xyz.luan.sabv.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

import xyz.luan.sabv.Validation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Validation({ byte[].class, short[].class, int[].class, long[].class, float[].class, double[].class, Object[].class, Collection.class })
public @interface Array {

    //perElementValidation();
    
}
