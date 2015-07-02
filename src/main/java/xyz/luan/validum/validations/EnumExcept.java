package xyz.luan.validum.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import xyz.luan.validum.Validation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
@Validation(Enum.class)
public @interface EnumExcept {

    String[] value() default {};

    public static class Validator implements xyz.luan.validum.AnnotationValidator<Enum<?>, EnumExcept> {

        @Override
        public List<String> validate(Enum<?> enumeration, EnumExcept annotation) {
            if (enumeration == null) {
                return Collections.emptyList();
            }

            boolean invalid = Arrays.asList(annotation.value()).contains(enumeration.name());
            if (!invalid) {
                return Collections.emptyList();
            }

            String allInvalid = Arrays.asList(annotation.value()).stream().collect(Collectors.joining(","));
            return Arrays.asList("EnumExcept.in{" + allInvalid + "}");
        }
    }
}