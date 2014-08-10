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
@Target({ ElementType.FIELD, ElementType.TYPE_USE })
@Validation(Enum.class)
public @interface EnumExcept {

    String[] value() default {};
    
    public static class Validator implements xyz.luan.sabv.Validator<Enum<?>, EnumExcept> {

        @Override
        public List<String> validate(Enum<?> enumeration, EnumExcept annotation) {
            if (enumeration == null) {
                return Collections.emptyList();
            }

            for (String t : annotation.value()) {
                if (enumeration.name().equalsIgnoreCase(t)) {
                    return Arrays.asList("EnumExcept.was{" + t + "}"); 
                }
            }
            
            return Collections.emptyList();
        }
    }
}