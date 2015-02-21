package xyz.luan.validum.util;

import java.util.Arrays;
import java.util.stream.Stream;

public class StreamUtil {

    private StreamUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @SafeVarargs
    public static <T> Stream<T> add(Stream<T> s, T... el) {
        return Stream.concat(s, toStream(el));
    }

    @SafeVarargs
    public static <T> Stream<T> addToBeginning(Stream<T> s, T... el) {
        return Stream.concat(toStream(el), s);
    }

    public static <T> Stream<T> add(T el, Stream<T> s) {
        return Stream.concat(toStream(el), s);
    }

    @SafeVarargs
    public static <T> Stream<T> toStream(T... t) {
        return Arrays.asList(t).stream();
    }
}
