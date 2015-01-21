package xyz.luan.validum.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import xyz.luan.validum.Validation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
@Validation(Enum.class)
public @interface EnumOnly {

    String[] value() default {};

    public static class Validator implements xyz.luan.validum.AnnotationValidator<Enum<?>, EnumOnly> {

        @Override
        public List<String> validate(Enum<?> enumeration, EnumOnly annotation) {
            if (enumeration == null) {
                return Collections.emptyList();
            }

            StringBuilder b = new StringBuilder();
            for (String t : annotation.value()) {
                if (enumeration.name().equalsIgnoreCase(t)) {
                    return Collections.emptyList();
                }
                b.append(t);
                b.append(",");
            }
            
            b.setLength(Math.max(b.length() - 1, 0));
            return Arrays.asList("EnumOnly.notIn{" + b + "}");
        }
    }
}