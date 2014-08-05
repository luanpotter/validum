package xyz.luan.sabv.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import xyz.luan.sabv.Validation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE })
@Validation(Object.class)
public @interface Required {
    
    public static class Validator implements xyz.luan.sabv.Validator<Object, Required> {

        @Override
        public List<String> validate(Object object, Required annotation) {
            return object == null ? Arrays.asList("Required.empty") : Collections.emptyList();
        }
    }

}
