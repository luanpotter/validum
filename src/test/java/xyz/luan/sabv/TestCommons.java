package xyz.luan.sabv;

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
        Assert.assertEquals(expected.length, actualList.size());
        if (expected.length == 0) {
            return;
        }

        String[] actual = actualList.toArray(new String [actualList.size()]);

        Arrays.sort(expected);
        Arrays.sort(actual);

        Assert.assertArrayEquals(expected, actual);
    }

}
