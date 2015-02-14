package xyz.luan.validum.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xyz.luan.validum.validations.Numeric;
import xyz.luan.validum.validations.Numeric.Cap;
import xyz.luan.validum.validations.Numeric.Type;

public final class NumberValidation {

    private NumberValidation() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<String> validate(Number number, Numeric annotation) {
        if (number == null) {
            return Collections.emptyList();
        }

        List<String> errors = new ArrayList<>();

        validateType(number, annotation, errors);
        validateMaxCap(number, annotation, errors);
        validateMinCap(number, annotation, errors);

        return errors;
    }

    private static void validateMinCap(Number number, Numeric annotation, List<String> errors) {
        if (annotation.minCap() == Cap.INCLUSIVE) {
            if (number.doubleValue() < annotation.min()) {
                errors.add("Numeric.smallerThan{" + annotation.min() + "}");
            }
        } else if (annotation.minCap() == Cap.EXCLUSIVE) {
            if (number.doubleValue() <= annotation.min()) {
                errors.add("Numeric.smallerOrEqualTo{" + annotation.min() + "}");
            }
        }
    }

    private static void validateMaxCap(Number number, Numeric annotation, List<String> errors) {
        if (annotation.maxCap() == Cap.INCLUSIVE) {
            if (number.doubleValue() > annotation.max()) {
                errors.add("Numeric.greaterThan{" + annotation.max() + "}");
            }
        } else if (annotation.maxCap() == Cap.EXCLUSIVE) {
            if (number.doubleValue() >= annotation.max()) {
                errors.add("Numeric.greaterOrEqualTo{" + annotation.max() + "}");
            }
        }
    }

    private static void validateType(Number number, Numeric annotation, List<String> errors) {
        if (annotation.type() == Type.INTEGER) {
            if (number.doubleValue() % 1 != 0) {
                errors.add("Numeric.notAnInteger");
            }
        }
    }

}
