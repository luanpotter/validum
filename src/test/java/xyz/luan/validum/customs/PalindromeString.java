package xyz.luan.validum.customs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import xyz.luan.validum.Validation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE_USE })
@Validation(String.class)
public @interface PalindromeString {

	public static class Validator implements xyz.luan.validum.AnnotationValidator<String, PalindromeString> {

		@Override
		public List<String> validate(String string, PalindromeString annotation) {
			if (string == null) {
				return Collections.emptyList();
			}

			int middle = string.length() / 2;
			for (int i = 0; i < middle; i++) {
				if (string.charAt(i) != string.charAt(string.length() - i - 1)) {
					return Arrays.asList(PalindromeString.class.getCanonicalName() + ".notPalindrome");
				}
			}
			return Collections.emptyList();
		}

	}
}
