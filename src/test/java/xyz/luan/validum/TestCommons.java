package xyz.luan.validum;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

public final class TestCommons {

    private TestCommons() {
        throw new RuntimeException("Should not be instanciated.");
    }

    public static void assertListEmpty(List<String> actualList) {
        assertListEquals(actualList);
    }

    public static void assertListEquals(List<String> actualList, String... expected) {
        String[] actual = actualList.toArray(new String [actualList.size()]);

        Arrays.sort(expected);
        Arrays.sort(actual);

        Assert.assertArrayEquals(expected, actual);
    }

}