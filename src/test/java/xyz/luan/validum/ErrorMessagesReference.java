package xyz.luan.validum;

import xyz.luan.validum.customs.PalindromeString;
import xyz.luan.validum.customs.ValidAddress;

public final class ErrorMessagesReference {

    private ErrorMessagesReference() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final String REQUIRED = "Required.empty";

    public static final String SMALLER_THAN = "Numeric.smallerThan";
    public static final String GREATER_THAN = "Numeric.greaterThan";

    public static final String LENGTH_BELOW = "Array.lengthBelow";
    public static final String LENGTH_ABOVE = "Array.lengthAbove";
    public static final String LENGTH_DIFFERS = "Array.lengthDiffers";

    public static final String ENUM_NOT_IN = "EnumOnly.notIn";
    public static final String ENUM_WAS = "EnumExcept.was";

    public static final String INVALID_ADDRESS = ValidAddress.class.getCanonicalName() + ".invalidAddress";
    public static final String NOT_PALINDROME = PalindromeString.class.getCanonicalName() + ".notPalindrome";
}
