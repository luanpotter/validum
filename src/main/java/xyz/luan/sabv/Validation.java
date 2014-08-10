package xyz.luan.sabv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Validation {

    public Class<?>[] value() default {};
    public DefaultTypes[] defaultType() default {};

    public enum DefaultTypes {
        ARRAY(byte[].class, short[].class, int[].class, long[].class, float[].class, double[].class, Object[].class, Iterable.class),
        NUMBER(int.class, Integer.class, short.class, Short.class, byte.class, Byte.class, float.class, Float.class, double.class, Double.class);

        private Class<?>[] classes;

        private DefaultTypes(Class<?>... classes) {
            this.classes = classes;
        }

        public static List<Class<?>> getClasses(Validation v) {
            List<Class<?>> classes = new ArrayList<>();
            classes.addAll(Arrays.asList(v.value()));
            Arrays.stream(v.defaultType()).forEach(dType -> classes.addAll(Arrays.asList(dType.classes)));
            return classes;
        }
    }
}
