package xyz.luan.sabv.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import xyz.luan.sabv.Validation;
import xyz.luan.sabv.Validation.DefaultTypes;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
@Validation(defaultType = { DefaultTypes.ARRAY }, value = { Map.class })
public @interface Array {

	int minLength() default -1;

	int maxLength() default -1;

	public static class Validator implements xyz.luan.sabv.AnnotationValidator<Object, Array> {

		@Override
		public List<String> validate(Object array, Array annotation) {
			if (array == null) {
				return Collections.emptyList();
			}

			int size = getArrayLength(array);
			List<String> errors = new ArrayList<>();

			if (annotation.minLength() != -1 && size < annotation.minLength()) {
				errors.add("Array.lengthBelow{" + annotation.minLength() + "}");
			}

			if (annotation.maxLength() != -1 && size > annotation.maxLength()) {
				errors.add("Array.lengthAbove{" + annotation.maxLength() + "}");
			}

			return errors;
		}

		public static int getArrayLength(Object array) {
			if (array.getClass().isArray()) {
				return java.lang.reflect.Array.getLength(array);
			}

			if (array instanceof Collection) {
				return Collection.class.cast(array).size();
			}

			if (array instanceof Map) {
				return Map.class.cast(array).size();
			}

			if (array instanceof Iterable) {
				Iterator<?> it = Iterable.class.cast(array).iterator();
				int i;
				for (i = 0; it.hasNext(); i++, it.next())
					;
				return i;
			}

			throw new RuntimeException("Unexpected array type for @Array. Must be either primitive array or a Collection.");
		}
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD, ElementType.TYPE_USE })
	@Validation(defaultType = { DefaultTypes.ARRAY })
	public @interface Fixed {
		int value();

		public static class Validator implements xyz.luan.sabv.AnnotationValidator<Object, Fixed> {

			@Override
			public List<String> validate(Object array, Fixed annotation) {
				if (array == null) {
					return Collections.emptyList();
				}
				int size = xyz.luan.sabv.validations.Array.Validator.getArrayLength(array);
				List<String> errors = new ArrayList<>();

				if (size != annotation.value()) {
					errors.add("Array.lengthDiffers{" + annotation.value() + "}");
				}

				return errors;
			}
		}
	}
}
