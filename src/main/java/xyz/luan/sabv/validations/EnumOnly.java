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
@Target(ElementType.FIELD)
@Validation(Enum.class)
public @interface EnumOnly {

    String[] value() default {};

    public static class Validator implements xyz.luan.sabv.Validator<Enum<?>, EnumOnly> {

        @Override
        public List<String> validate(Enum<?> enumeration, EnumOnly annotation) {
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