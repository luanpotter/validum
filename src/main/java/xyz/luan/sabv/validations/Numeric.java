package xyz.luan.sabv.validations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xyz.luan.sabv.Validation;
import xyz.luan.sabv.Validation.DefaultTypes;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE_USE })
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
    
    public static class Validator implements xyz.luan.sabv.Validator<Number, Numeric> {
        
        @Override
        public List<String> validate(Number number, Numeric annotation) {
            return genericValidate(number, annotation);
        }
        
        public static List<String> genericValidate(Number number, Numeric annotation) {
            if (number == null) {
                return Collections.emptyList();
            }

            List<String> errors = new ArrayList<>();
            if (annotation.type() == Type.INTEGER) {
                if (number.doubleValue() % 1 != 0)
                    errors.add("Numeric.notAnInteger");
            }
            
            if (annotation.maxCap() == Cap.INCLUSIVE) {
                if (number.doubleValue() > annotation.max()) {
                    errors.add("Numeric.greaterThan{" + annotation.max() + "}");
                }
            } else if (annotation.maxCap() == Cap.EXCLUSIVE) {
                if (number.doubleValue() >= annotation.max()) {
                    errors.add("Numeric.greaterOrEqualTo{" + annotation.max() + "}");
                }
            }
            
            if (annotation.minCap() == Cap.INCLUSIVE) {
                if (number.doubleValue() < annotation.min()) {
                    errors.add("Numeric.smallerThan{" + annotation.min() + "}");
                }
            } else if (annotation.minCap() == Cap.EXCLUSIVE) {
                if (number.doubleValue() <= annotation.min()) {
                    errors.add("Numeric.smallerOrEqualTo{" + annotation.min() + "}");
                }
            }
            
            return errors;
        }
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD, ElementType.TYPE_USE })
    @Validation(defaultType = { DefaultTypes.NUMBER })
    public @interface Natural {
        
        public static class Validator implements xyz.luan.sabv.Validator<Number, Natural> {
            
            @Override
            public List<String> validate(Number number, Natural annotation) {
                return Numeric.Validator.genericValidate(number, new Numeric() {

                    @Override
                    public Class<? extends Annotation> annotationType() {
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
        
        public static class Validator implements xyz.luan.sabv.Validator<Number, Min> {
        
            @Override
            public List<String> validate(Number number, Min annotation) {
                return Numeric.Validator.genericValidate(number, new Numeric() {
    
                    @Override
                    public Class<? extends Annotation> annotationType() {
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
        
        public static class Validator implements xyz.luan.sabv.Validator<Number, Max> {
            
            @Override
            public List<String> validate(Number number, Max annotation) {
                return Numeric.Validator.genericValidate(number, new Numeric() {
    
                    @Override
                    public Class<? extends Annotation> annotationType() {
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
