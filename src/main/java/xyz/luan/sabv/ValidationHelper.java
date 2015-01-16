package xyz.luan.sabv;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import xyz.luan.reflection.ReflectionUtils;
import xyz.luan.reflection.tclass.TypedClass;
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

	public static List<Annotation> getValidationAnnotations(Class<?> clazz) {
		return getValidationAnnotations(clazz.getAnnotations());
	}

	private static List<Annotation> getValidationAnnotations(Annotation[] annotations) {
		return Arrays.stream(annotations).filter((ann) -> isValidationAnnotation(ann)).collect(Collectors.toList());
	}

	private static List<String> validateValueWith(Object value, Annotation annotation) {
		if (!isValidAnnotationType(value, annotation)) {
			final String message = "Validation annotation %s used on unsupported type %s. Check documentation for a list of suported types.";
			throw new ValidationException(String.format(message, annotation, value.getClass()));
		}

		Validator validator = VALIDATORS.get(annotation.annotationType());
		if (validator == null) {
			throw new ValidationException("No validator added to this annotation! The annotation " + annotation + " does not have a validator.");
		}

		return validator.validate(value, annotation);
	}

	private static boolean isValidAnnotationType(Object value, Annotation annotation) {
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
		return validate(new ArrayList<>(), ":", obj, TypedClass.create(obj.getClass()), getValidationAnnotations(obj.getClass()));
	}

	public static List<String> validate(List<String> errors, String errorPrefix, Object obj, TypedClass<?> clazz, List<Annotation> globalValidations) {
		if (obj != null) {
			parseFields(errors, errorPrefix, obj);
			parseChildren(errors, errorPrefix, clazz, obj);
		}

		for (Annotation ann : globalValidations) {
			validateValueWith(obj, ann).forEach((error) -> errors.add(errorPrefix + error));
		}

		return errors;
	}

	private static void parseFields(List<String> errors, String errorPrefix, Object obj) {
		List<Field> fields = ReflectionUtils.getFieldsRecursivelyExceptJavaClasses(obj.getClass());
		for (Field f : fields) {
			TypedClass<?> c = TypedClass.create(f);
			String fieldPrefix = errorPrefix + f.getName() + ":";
			Object fieldValue = getFieldValue(obj, f);

			validate(errors, fieldPrefix, fieldValue, c, getGlobals(c));
		}
	}

	private static void parseChildren(List<String> errors, String errorPrefix, TypedClass<?> c, Object value) {
		if (c.isList()) {
			// TODO option to use index or stringfy here
			c.asList().forEach(value,
					(i, el) -> validate(errors, errorPrefix + "[" + i + "]:", el, c.asList().getComponent(), getGlobals(c.asList().getComponent())));
		} else if (c.isMap()) {
			// TODO customize stringfy
			TypedClass<?> keyType = c.asMap().getKey(), valueType = c.asMap().getValue();
			c.asMap().forEach(value, (k, v) -> {
				validate(errors, errorPrefix + "[" + stringfy(k) + "]:", k, keyType, getGlobals(keyType));
				validate(errors, errorPrefix + "[" + stringfy(k) + "]:", v, valueType, getGlobals(valueType));
			});
		}
	}

	private static List<Annotation> getGlobals(TypedClass<?> c) {
		final List<Annotation> globalForField = getValidationAnnotations(c.asClass());
		c.getAnnotations().stream().filter((a) -> isValidationAnnotation(a)).forEach((a -> globalForField.add(a)));
		return globalForField;
	}

	private static String stringfy(Object element) {
		return element == null ? null : element.toString().replace("[", "\\[").replace("]", "\\]");
	}

	private static Object getFieldValue(Object obj, Field f) {
		f.setAccessible(true);
		try {
			return f.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException("Totally unexpected expection!", e);
		}
	}
}
