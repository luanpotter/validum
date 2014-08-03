package xyz.luan.sabv;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import xyz.luan.sabv.validations.Required;

public final class ValidationHelper {

    public static final Map<Class<? extends Annotation>, Validator<?, ? extends Annotation>> VALIDATORS = new HashMap<>();
    static {
        VALIDATORS.put(Required.class, new Validator<Object, Required>() {

            @Override
            public String validate(Object object, Required annotation) {
                return object == null ? "Required.empty" : null;
            }
            
        });
    }
    
    private ValidationHelper() {
        throw new RuntimeException("Should not be instancited");
    }

    private static List<Field> getFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (!isJavaClass(clazz)) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()).stream().filter((Field f) -> !Modifier.isStatic(f.getModifiers())).collect(Collectors.toList()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    private static boolean isJavaClass(Class<?> clazz) {
        return Object.class.equals(clazz) || clazz.isPrimitive() || clazz.isEnum() || clazz.isArray() || clazz.getPackage().getName().startsWith("java.") || clazz.getPackage().getName().startsWith("javax.");
    }

    private static List<Annotation> getValidationAnnotations(Class<?> clazz) {
        return getValidationAnnotations(clazz.getAnnotations());
    }

    private static List<Annotation> getValidationAnnotations(Annotation[] annotations) {
        List<Annotation> validations = new ArrayList<>();
        for (Annotation ann : annotations) {
            if (ann.annotationType().getAnnotation(Validation.class) != null) {
                validations.add(ann);
            }
        }
        return validations;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static String validateValueWith(Object value, Annotation annotation) {
        Validator validator = VALIDATORS.get(annotation.annotationType());
        if (validator == null) {
            throw new ValidationException("No validator added to this annotation! The annotation " + annotation + " does not have a validator.");
        }
        return validator.validate(value, annotation);
    }

    public static List<String> validate(Object obj) {
        return validate(":", obj, getValidationAnnotations(obj.getClass()));
    }

    public static List<String> validate(String errorPrefix, Object obj, List<Annotation> globalValidations) {
        List<String> errors = new ArrayList<>();

        if (obj != null) {
            List<Field> fields = getFields(obj.getClass());
            for (Field f : fields) {
                List<Annotation> globalForField = getAllGlobalValidationsForType(f);
                errors.addAll(validate(errorPrefix + f.getName() + ":", getFieldValue(obj, f), globalForField));
            }
        }

        for (Annotation ann : globalValidations) {
            if (errors.isEmpty() || !ann.annotationType().getAnnotation(Validation.class).requireValidFields()) {
                String error = validateValueWith(obj, ann);
                if (error != null) {
                    errors.add(errorPrefix + error);
                }
            }
        }

        return errors;
    }

    private static List<Annotation> getAllGlobalValidationsForType(Field f) {
        List<Annotation> globalForField = getValidationAnnotations(f.getType());
        globalForField.addAll(getValidationAnnotations(f.getAnnotations()));
        return globalForField;
    }

    private static Object getFieldValue(Object obj, Field f) {
        f.setAccessible(true);
        Object fieldValue;
        try {
            fieldValue = f.get(obj);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("Totally unexpected expection!", e);
        }
        return fieldValue;
    }
}
