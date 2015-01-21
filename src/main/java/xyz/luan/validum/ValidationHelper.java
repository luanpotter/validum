package xyz.luan.validum;

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
import xyz.luan.validum.validations.EnumExcept;
import xyz.luan.validum.validations.EnumOnly;
import xyz.luan.validum.validations.Numeric;
import xyz.luan.validum.validations.Required;

public final class ValidationHelper {

	private ValidationHelper() {
		throw new RuntimeException("Should not be instancited");
	}

	public static boolean isValidationAnnotation(Annotation ann) {
		return ann.annotationType().getAnnotation(Validation.class) != null;
	}

	public static List<Annotation> getValidationAnnotations(Class<?> clazz) {
		return getValidationAnnotations(clazz.getAnnotations());
	}

	public static List<Annotation> getValidationAnnotations(Annotation[] annotations) {
		return Arrays.stream(annotations).filter((ann) -> isValidationAnnotation(ann)).collect(Collectors.toList());
	}

	public static boolean isValidAnnotationType(Object value, Annotation annotation) {
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

	public static List<Annotation> getGlobals(TypedClass<?> c) {
		final List<Annotation> globalForField = getValidationAnnotations(c.asClass());
		c.getAnnotations().stream().filter((a) -> isValidationAnnotation(a)).forEach((a -> globalForField.add(a)));
		return globalForField;
	}

	public static Object getFieldValue(Object obj, Field f) {
		f.setAccessible(true);
		try {
			return f.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException("Totally unexpected expection!", e);
		}
	}
}
