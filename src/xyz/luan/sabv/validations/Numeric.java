package xyz.luan.sabv.validations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import xyz.luan.sabv.Validation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Validation({ int.class, Integer.class, short.class, Short.class, byte.class, Byte.class, float.class, Float.class, double.class, Double.class })
public @interface Numeric {

    Type type() default Type.DEFAULT;
    
    int min() default 0;
    int max() default 0;

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
        public String validate(Number number, Numeric annotation) {
            return genericValidate(number, annotation);
        }
        
        public static String genericValidate(Number number, Numeric annotation) {
            if (annotation.type() == Type.INTEGER) {
                if (number.doubleValue() % 1 != 0)
                    return "Numeric.notAnInteger";
            }
            
            if (annotation.maxCap() == Cap.INCLUSIVE) {
                if (number.doubleValue() > annotation.max()) {
                    return "Numeric.greaterThan{" + annotation.max() + "}";
                }
            } else if (annotation.maxCap() == Cap.EXCLUSIVE) {
                if (number.doubleValue() >= annotation.max()) {
                    return "Numeric.greaterOrEqualTo{" + annotation.max() + "}";
                }
            }
            
            if (annotation.minCap() == Cap.INCLUSIVE) {
                if (number.doubleValue() < annotation.min()) {
                    return "Numeric.smallerThan{" + annotation.min() + "}";
                }
            } else if (annotation.minCap() == Cap.EXCLUSIVE) {
                if (number.doubleValue() <= annotation.min()) {
                    return "Numeric.smallerOrEqualTo{" + annotation.min() + "}";
                }
            }
            
            return null;
        }
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @Validation({ int.class, Integer.class, short.class, Short.class, byte.class, Byte.class, float.class, Float.class, double.class, Double.class })
    public @interface Natural {
        
        public static class Validator implements xyz.luan.sabv.Validator<Number, Natural> {
            
            @Override
            public String validate(Number number, Natural annotation) {
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
                    public int min() {
                        return 0;
                    }

                    @Override
                    public int max() {
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
    @Target(ElementType.FIELD)
    @Validation({ int.class, Integer.class, short.class, Short.class, byte.class, Byte.class, float.class, Float.class, double.class, Double.class })
    public @interface Min {
        int value();
        Cap cap() default Cap.INCLUSIVE;
        
        public static class Validator implements xyz.luan.sabv.Validator<Number, Min> {
        
            @Override
            public String validate(Number number, Min annotation) {
                return Numeric.Validator.genericValidate(number, new Numeric() {
    
                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return Numeric.class;
                    }
                    
                    @Override
                    public int min() {
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
                    public int max() {
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
    @Target(ElementType.FIELD)
    @Validation({ int.class, Integer.class, short.class, Short.class, byte.class, Byte.class, float.class, Float.class, double.class, Double.class })
    public @interface Max {
        int value();
        Cap cap() default Cap.INCLUSIVE;
        
        public static class Validator implements xyz.luan.sabv.Validator<Number, Max> {
            
            @Override
            public String validate(Number number, Max annotation) {
                return Numeric.Validator.genericValidate(number, new Numeric() {
    
                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return Numeric.class;
                    }
                    
                    @Override
                    public int max() {
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
                    public int min() {
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
