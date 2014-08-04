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

import xyz.luan.sabv.validations.EnumExcept;
import xyz.luan.sabv.validations.EnumOnly;
import xyz.luan.sabv.validations.Numeric;
import xyz.luan.sabv.validations.Required;

public final class ValidationHelper {

    public static final Map<Class<? extends Annotation>, Validator<?, ? extends Annotation>> VALIDATORS = new HashMap<>();
    static {
        VALIDATORS.put(Required.class, new Required.Validator());
        VALIDATORS.put(Numeric.class, new Numeric.Validator());
        VALIDATORS.put(Numeric.Natural.class, new Numeric.Natural.Validator());
        VALIDATORS.put(Numeric.Min.class, new Numeric.Min.Validator());
        VALIDATORS.put(Numeric.Max.class, new Numeric.Max.Validator());
        VALIDATORS.put(EnumOnly.class, new EnumOnly.Validator());
        VALIDATORS.put(EnumExcept.class, new EnumExcept.Validator());
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
    private static List<String> validateValueWith(Object value, Annotation annotation) {
        int descendLevel = getDescendLevelForValue(value, annotation);
        if (descendLevel == -1) {
            throw new ValidationException("Validation annotation " + annotation + " used on unsupported type " + value.getClass() + ". Check documentation for a list of suported types.");
        }

        Validator validator = VALIDATORS.get(annotation.annotationType());
        if (validator == null) {
            throw new ValidationException("No validator added to this annotation! The annotation " + annotation + " does not have a validator.");
        }
        
        if (descendLevel == 0) {
            return validator.validate(value, annotation);
        }

        //TODO array
        Object currentValue = value;
        return validator.validate(currentValue, annotation);
    }

    private static int getDescendLevelForValue(Object value, Annotation annotation) {
        if (value == null) {
            return 0;
        }
        
        return getDescentLevelForType(value.getClass(), annotation);
    }
    
    private static int getDescentLevelForType(Class<?> typeClass, Annotation annotation) {
        for (Class<?> clazz : annotation.annotationType().getAnnotation(Validation.class).value()) {
            if (clazz.isAssignableFrom(typeClass)) {
                return 0;
            }
        }
        
        if (typeClass.isArray()) {
            return 1 + getDescentLevelForType(typeClass.getComponentType(), annotation);
        }

        return -1;
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
            if (errors.isEmpty()) {
                List<String> currentErrors = validateValueWith(obj, ann);
                for (String error : currentErrors) {
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
