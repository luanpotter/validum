package xyz.luan.validum.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import xyz.luan.validum.Validation;
import xyz.luan.validum.Validation.DefaultTypes;
import xyz.luan.validum.util.NumberValidation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
@Validation(defaultType = { DefaultTypes.NUMBER })
public @interface Numeric {

    Type type() default Type.DEFAULT;

    double min() default .0;

    double max() default .0;

    Cap minCap() default Cap.INCLUSIVE;

    Cap maxCap() default Cap.NONE;

    public enum Type {
        DEFAULT, INTEGER, REAL;
    }

    public enum Cap {
        INCLUSIVE, EXCLUSIVE, NONE;
    }

    public static class Validator implements xyz.luan.validum.AnnotationValidator<Number, Numeric> {

        @Override
        public List<String> validate(Number number, Numeric annotation) {
            return NumberValidation.validate(number, annotation);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD, ElementType.TYPE_USE })
    @Validation(defaultType = { DefaultTypes.NUMBER })
    public @interface Natural {

        public static class Validator implements xyz.luan.validum.AnnotationValidator<Number, Natural> {

            @Override
            public List<String> validate(Number number, Natural annotation) {
                return NumberValidation.validate(number, new Numeric() {

                    @Override
                    public Class<Numeric> annotationType() {
                        return Numeric.class;
                    }

                    @Override
                    public Type type() {
                        return Type.INTEGER;
                    }

                    @Override
                    public double min() {
                        return 0;
                    }

                    @Override
                    public double max() {
                        return 0;
                    }

                    @Override
                    public Cap minCap() {
                        return Cap.INCLUSIVE;
                    }

                    @Override
                    public Cap maxCap() {
                        return Cap.NONE;
                    }
                });
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD, ElementType.TYPE_USE })
    @Validation(defaultType = { DefaultTypes.NUMBER })
    public @interface Min {
        double value();

        Cap cap() default Cap.INCLUSIVE;

        public static class Validator implements xyz.luan.validum.AnnotationValidator<Number, Min> {

            @Override
            public List<String> validate(Number number, Min annotation) {
                return NumberValidation.validate(number, new Numeric() {

                    @Override
                    public Class<Numeric> annotationType() {
                        return Numeric.class;
                    }

                    @Override
                    public double min() {
                        return annotation.value();
                    }

                    @Override
                    public Cap minCap() {
                        return annotation.cap();
                    }

                    @Override
                    public Type type() {
                        return Type.REAL;
                    }

                    @Override
                    public double max() {
                        return 0;
                    }

                    @Override
                    public Cap maxCap() {
                        return Cap.NONE;
                    }
                });
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD, ElementType.TYPE_USE })
    @Validation(defaultType = { DefaultTypes.NUMBER })
    public @interface Max {
        double value();

        Cap cap() default Cap.INCLUSIVE;

        public static class Validator implements xyz.luan.validum.AnnotationValidator<Number, Max> {

            @Override
            public List<String> validate(Number number, Max annotation) {
                return NumberValidation.validate(number, new Numeric() {

                    @Override
                    public Class<Numeric> annotationType() {
                        return Numeric.class;
                    }

                    @Override
                    public double max() {
                        return annotation.value();
                    }

                    @Override
                    public Cap maxCap() {
                        return annotation.cap();
                    }

                    @Override
                    public Type type() {
                        return Type.REAL;
                    }

                    @Override
                    public double min() {
                        return 0;
                    }

                    @Override
                    public Cap minCap() {
                        return Cap.NONE;
                    }
                });
            }
        }
    }
}
