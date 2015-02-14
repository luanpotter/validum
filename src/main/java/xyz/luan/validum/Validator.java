package xyz.luan.validum;

import static xyz.luan.validum.ValidationHelper.getFieldValue;
import static xyz.luan.validum.ValidationHelper.getGlobals;
import static xyz.luan.validum.ValidationHelper.getValidationAnnotations;
import static xyz.luan.validum.ValidationHelper.isValidAnnotationType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import xyz.luan.reflection.ReflectionUtils;
import xyz.luan.reflection.tclass.TypedClass;
import xyz.luan.validum.validations.EnumExcept;
import xyz.luan.validum.validations.EnumOnly;
import xyz.luan.validum.validations.Numeric;
import xyz.luan.validum.validations.Required;

public class Validator {

    private Map<Class<? extends Annotation>, AnnotationValidator<?, ? extends Annotation>> annotationValidators;
    private boolean useIndexForLists;
    private Function<Object, String> stringfy;

    private Validator() {
        annotationValidators = new HashMap<>();
        stringfy = element -> element == null ? null : element.toString().replace("[", "\\[").replace("]", "\\]");
        useIndexForLists = true;
    }

    public static Validator empty() {
        return new Validator();
    }

    public static Validator withDefaults() {
        return new Validator().defaultValidators();
    }

    public <T extends Annotation> Validator addValidation(Class<T> clazz, AnnotationValidator<?, T> validator) {
        annotationValidators.put(clazz, validator);
        return this;
    }

    public Validator defaultValidators() {
        addValidation(Required.class, new Required.Validator());
        addValidation(Numeric.class, new Numeric.Validator());
        addValidation(Numeric.Natural.class, new Numeric.Natural.Validator());
        addValidation(Numeric.Min.class, new Numeric.Min.Validator());
        addValidation(Numeric.Max.class, new Numeric.Max.Validator());
        addValidation(EnumOnly.class, new EnumOnly.Validator());
        addValidation(EnumExcept.class, new EnumExcept.Validator());
        addValidation(xyz.luan.validum.validations.Array.class, new xyz.luan.validum.validations.Array.Validator());
        addValidation(xyz.luan.validum.validations.Array.Fixed.class, new xyz.luan.validum.validations.Array.Fixed.Validator());
        return this;
    }

    public List<String> validate(Object obj) {
        return validate(new ArrayList<>(), ":", obj, TypedClass.create(obj.getClass()), getValidationAnnotations(obj.getClass()));
    }

    private List<String> validate(List<String> errors, String errorPrefix, Object obj, TypedClass<?> clazz, List<Annotation> globalValidations) {
        if (obj != null) {
            parseFields(errors, errorPrefix, obj);
            parseChildren(errors, errorPrefix, clazz, obj);
        }

        for (Annotation ann : globalValidations) {
            validateValueWith(obj, ann).forEach((error) -> errors.add(errorPrefix + error));
        }

        return errors;
    }

    private void parseFields(List<String> errors, String errorPrefix, Object obj) {
        List<Field> fields = ReflectionUtils.getFieldsRecursivelyExceptJavaClasses(obj.getClass());
        for (Field f : fields) {
            TypedClass<?> c = TypedClass.create(f);
            String fieldPrefix = errorPrefix + f.getName() + ":";
            Object fieldValue = getFieldValue(obj, f);

            validate(errors, fieldPrefix, fieldValue, c, getGlobals(c));
        }
    }

    private void parseChildren(List<String> errors, String errorPrefix, TypedClass<?> c, Object value) {
        if (c.isList()) {
            c.asList().forEach(value, (i, el) -> {
                String newPrefix = errorPrefix + "[" + (useIndexForLists ? i : stringfy.apply(el)) + "]:";
                validate(errors, newPrefix, el, c.asList().getComponent(), getGlobals(c.asList().getComponent()));
            });
        } else if (c.isMap()) {
            TypedClass<?> keyType = c.asMap().getKey(), valueType = c.asMap().getValue();
            c.asMap().forEach(value, (k, v) -> {
                validate(errors, errorPrefix + "[" + stringfy.apply(k) + "]:", k, keyType, getGlobals(keyType));
                validate(errors, errorPrefix + "[" + stringfy.apply(k) + "]:", v, valueType, getGlobals(valueType));
            });
        }
    }

    // TODO review these warnings
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<String> validateValueWith(Object value, Annotation annotation) {
        if (!isValidAnnotationType(value, annotation)) {
            final String message = "Validation annotation %s used on unsupported type %s. Check documentation for a list of suported types.";
            throw new ValidationSetupException(String.format(message, annotation, value.getClass()));
        }

        AnnotationValidator validator = annotationValidators.get(annotation.annotationType());
        if (validator == null) {
            final String message = "No validator added to this annotation! The annotation @%s does not have a validator.";
            throw new ValidationSetupException(String.format(message, annotation.annotationType().getCanonicalName()));
        }

        return validator.validate(value, annotation);
    }

}
