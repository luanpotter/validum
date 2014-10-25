package xyz.luan.sabv;

import static java.util.Arrays.stream;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import xyz.luan.reflection.ReflectionUtils;
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
        VALIDATORS.put(xyz.luan.sabv.validations.Array.class, new xyz.luan.sabv.validations.Array.Validator());
        VALIDATORS.put(xyz.luan.sabv.validations.Array.Fixed.class, new xyz.luan.sabv.validations.Array.Fixed.Validator());
    }
    
    private ValidationHelper() {
        throw new RuntimeException("Should not be instancited");
    }

    public static boolean isValidationAnnotation(Annotation ann) {
        return ann.annotationType().getAnnotation(Validation.class) != null;
    }

    static List<Annotation> getValidationAnnotations(Class<?> clazz) {
        return getValidationAnnotations(clazz.getAnnotations());
    }

    static List<Annotation> getValidationAnnotations(Annotation[] annotations) {
        List<Annotation> validations = new ArrayList<>();
        for (Annotation ann : annotations) {
            if (isValidationAnnotation(ann)) {
                validations.add(ann);
            }
        }
        return validations;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    static List<String> validateValueWith(Object value, Annotation annotation) {
        if (!isValidAnnotationType(value, annotation)) {
            throw new ValidationException("Validation annotation " + annotation + " used on unsupported type " + value.getClass() + ". Check documentation for a list of suported types.");
        }

        Validator validator = VALIDATORS.get(annotation.annotationType());
        if (validator == null) {
            throw new ValidationException("No validator added to this annotation! The annotation " + annotation + " does not have a validator.");
        }

        return validator.validate(value, annotation);
    }

    static boolean isValidAnnotationType(Object value, Annotation annotation) {
        if (value == null) {
            return true;
        }
        
        List<Class<?>> validClasses = Validation.DefaultTypes.getClasses(annotation.annotationType().getAnnotation(Validation.class));
        for (Class<?> clazz : validClasses) {
            if (clazz.isAssignableFrom(value.getClass())) {
                return true;
            }
        }
        return false;
    }

    public static List<String> validate(Object obj) {
        return validate(":", obj, getValidationAnnotations(obj.getClass()));
    }

    public static List<String> validate(String errorPrefix, Object obj, List<Annotation> globalValidations) {
        List<String> errors = new ArrayList<>();

        if (obj != null) {
            List<Field> fields = ReflectionUtils.getFieldsRecursivelyExceptJavaClasses(obj.getClass());
            for (Field f : fields) {
                List<Annotation> globalForField = getAllGlobalValidationsForType(f);
                String fieldPrefix = errorPrefix + f.getName() + ":";
                Object fieldValue = getFieldValue(obj, f);
                errors.addAll(validate(fieldPrefix, fieldValue, globalForField));

                if (fieldValue != null) {
                    errors.addAll(getErrorsFromElements(fieldPrefix, fieldValue, f.getAnnotatedType()));
                }
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

    static List<String> getErrorsFromElements(String fieldPrefix, Object currentLevelFieldValue, AnnotatedType at) {
        List<String> errors = new ArrayList<>();
        if (currentLevelFieldValue.getClass().isArray()) {
            AnnotatedArrayType annotatedType = (AnnotatedArrayType) at;
            List<Annotation> globalsForElement = getValidationAnnotationsFrom(annotatedType.getAnnotations());

            for (int i = 0; i < Array.getLength(currentLevelFieldValue); i++) {
                Object element = Array.get(currentLevelFieldValue, i);
                String elementPrefix = fieldPrefix + "[" + i + "]:";
                errors.addAll(validate(elementPrefix, element, globalsForElement));
                if (element != null) {
                    errors.addAll(getErrorsFromElements(elementPrefix, element, annotatedType.getAnnotatedGenericComponentType()));
                }
            }
        } else if (List.class.isAssignableFrom(currentLevelFieldValue.getClass())) {
            AnnotatedParameterizedType annotatedType = (AnnotatedParameterizedType) at;

            assert annotatedType.getAnnotatedActualTypeArguments().length == 1;
            AnnotatedType elementAnnotedType = annotatedType.getAnnotatedActualTypeArguments()[0];
            List<Annotation> globalsForElement = getValidationAnnotationsFrom(elementAnnotedType.getAnnotations());

            ListIterator<?> it = List.class.cast(currentLevelFieldValue).listIterator();
            int count = 0;
            while (it.hasNext()) {
                Object element = it.next();
                String elementPrefix = fieldPrefix + "[" + count++ + "]:";

                errors.addAll(validate(elementPrefix, element, globalsForElement));

                assert annotatedType.getAnnotatedActualTypeArguments().length == 1;
                if (element != null) {
                    errors.addAll(getErrorsFromElements(elementPrefix, element, annotatedType.getAnnotatedActualTypeArguments()[0]));
                }
            }
        } else if (Collection.class.isAssignableFrom(currentLevelFieldValue.getClass())) {
            AnnotatedParameterizedType annotatedType = (AnnotatedParameterizedType) at;

            assert annotatedType.getAnnotatedActualTypeArguments().length == 1;
            AnnotatedType elementAnnotedType = annotatedType.getAnnotatedActualTypeArguments()[0];
            List<Annotation> globalsForElement = getValidationAnnotationsFrom(elementAnnotedType.getAnnotations());

            Iterable<?> it = Collection.class.cast(currentLevelFieldValue);
            it.forEach(element -> {
                String elementPrefix = fieldPrefix + "[" + stringfy(element) + "]:";
                errors.addAll(validate(elementPrefix, element, globalsForElement));
                if (element != null) {
                    errors.addAll(getErrorsFromElements(elementPrefix, element, elementAnnotedType));
                }
            });
        } else if (Map.class.isAssignableFrom(currentLevelFieldValue.getClass())) {
            AnnotatedParameterizedType annotatedType = (AnnotatedParameterizedType) at;

            assert annotatedType.getAnnotatedActualTypeArguments().length == 2;
            AnnotatedType[] elementAnnotedTypes = annotatedType.getAnnotatedActualTypeArguments();

            List<Annotation> globalsForElement0 = getValidationAnnotationsFrom(elementAnnotedTypes[0].getAnnotations());
            List<Annotation> globalsForElement1 = getValidationAnnotationsFrom(elementAnnotedTypes[1].getAnnotations());

            Map<?, ?> map = Map.class.cast(currentLevelFieldValue);
            Set<?> keySet = map.keySet();
            for (Object element : keySet) {
                String elementPrefix = fieldPrefix + "[" + stringfy(element) + "]:";
                errors.addAll(validate(elementPrefix, element, globalsForElement0));
                if (element != null) {
                    errors.addAll(getErrorsFromElements(elementPrefix, element, elementAnnotedTypes[0]));
                }

                errors.addAll(validate(elementPrefix, map.get(element), globalsForElement1));
                if (map.get(element) != null) {
                    errors.addAll(getErrorsFromElements(elementPrefix, map.get(element), elementAnnotedTypes[1]));
                }
            };
        }

        return errors;
    }

    private static String stringfy(Object element) {
        return element.toString().replace("[", "\\[").replace("]", "\\]");
    }

    static List<Annotation> getValidationAnnotationsFrom(
            Annotation[] allList) {
        return stream(allList).filter(ValidationHelper::isValidationAnnotation).collect(Collectors.toList());
    }

    static List<Annotation> getAllGlobalValidationsForType(Field f) {
        List<Annotation> globalForField = getValidationAnnotations(f.getType());
        globalForField.addAll(getValidationAnnotations(f.getAnnotations()));
        return globalForField;
    }

    static Object getFieldValue(Object obj, Field f) {
        f.setAccessible(true);
        try {
            return f.get(obj);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("Totally unexpected expection!", e);
        }
    }
}
