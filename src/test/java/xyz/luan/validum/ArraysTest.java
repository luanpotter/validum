package xyz.luan.validum;

import static xyz.luan.validum.ErrorMessagesReference.GREATER_THAN;
import static xyz.luan.validum.ErrorMessagesReference.LENGTH_ABOVE;
import static xyz.luan.validum.ErrorMessagesReference.LENGTH_BELOW;
import static xyz.luan.validum.ErrorMessagesReference.LENGTH_DIFFERS;
import static xyz.luan.validum.ErrorMessagesReference.NOT_PALINDROME;
import static xyz.luan.validum.ErrorMessagesReference.REQUIRED;
import static xyz.luan.validum.ErrorMessagesReference.SMALLER_THAN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import xyz.luan.validum.customs.PalindromeString;
import xyz.luan.validum.entities.God;
import xyz.luan.validum.entities.Power;
import xyz.luan.validum.entities.Weakness;

public class ArraysTest extends BaseTest {

    @Test
    public void testArraysFirstLevelAnnotationsInvalid() {
        God god = new God("Poseidon", null, null, null, null);
        List<String> errors = validator.validate(god);
        assertListEquals(errors, ":powers:" + REQUIRED, ":weaknesses:" + REQUIRED, ":calculationMatrix:" + REQUIRED);
    }

    @Test
    public void testArraysSecondLevelAnnotationsInvalid() {
        God god = new God("Poseidon", new Power[] { null }, new ArrayList<Weakness>() {
            private static final long serialVersionUID = -7219674458766897456L;
            {
                add(Weakness.CATS);
                add(null);
                add(Weakness.KRYPTONITE);
            }
        }, null, null);
        List<String> errors = validator.validate(god);
        assertListEquals(errors, new String[] {
            ":powers:" + LENGTH_BELOW + "{2}",
            ":powers:[0]:" + REQUIRED,
            ":weaknesses:" + LENGTH_ABOVE + "{2}",
            ":weaknesses:[1]:" + REQUIRED,
            ":calculationMatrix:" + REQUIRED });
    }

    @Test
    public void testArraysThirdLevelAnnotationsInvalid() {
        God god = new God(null, new Power[] { Power.FLIGHT, Power.IMMORTALITY }, new ArrayList<Weakness>(), new float[][] {
            { 2f, 3.5f, 4f, 3.7f },
            { 2f, 3.7f, 4.1f, 3.2f, 3.2f },
            { 4f, 4f, 3.8f, -1f },
            { 4f, 4.5f, 3.6f, 3.7f, 0 } }, null);
        List<String> errors = validator.validate(god);
        assertListEquals(errors, new String[] {
            ":calculationMatrix:" + LENGTH_DIFFERS + "{3}",
            ":calculationMatrix:[0]:[0]:" + SMALLER_THAN + "{3.5}",
            ":calculationMatrix:[1]:" + LENGTH_DIFFERS + "{4}",
            ":calculationMatrix:[1]:[0]:" + SMALLER_THAN + "{3.5}",
            ":calculationMatrix:[1]:[2]:" + GREATER_THAN + "{4.0}",
            ":calculationMatrix:[1]:[3]:" + SMALLER_THAN + "{3.5}",
            ":calculationMatrix:[1]:[4]:" + SMALLER_THAN + "{3.5}",
            ":calculationMatrix:[2]:[3]:" + SMALLER_THAN + "{3.5}",
            ":calculationMatrix:[3]:" + LENGTH_DIFFERS + "{4}",
            ":calculationMatrix:[3]:[1]:" + GREATER_THAN + "{4.0}",
            ":calculationMatrix:[3]:[4]:" + SMALLER_THAN + "{3.5}" });
    }

    @Test
    public void testArraysThirdLevelAnnotationsValid() {
        God god = new God(null, new Power[] { Power.FLIGHT, Power.IMMORTALITY }, new ArrayList<Weakness>(), new float[][] {
            { 3.7f, 3.5f, 4f, 3.641f },
            { 3.7f, 3.7f, 4.0f, 3.622f },
            { 4f, 4f, 3.8f, 3.94f } }, null);
        List<String> errors = validator.validate(god);
        assertListEmpty(errors);
    }

    @Test
    public void testMapFirstLevel() {
        God god = new God(null, new Power[] { Power.FLIGHT, Power.IMMORTALITY }, new ArrayList<Weakness>(), new float[][] {
            { 3.7f, 3.5f, 4f, 3.641f },
            { 3.7f, 3.7f, 4.0f, 3.622f },
            { 4f, 4f, 3.8f, 3.94f } }, new HashMap<>());
        List<String> errors = validator.validate(god);
        assertListEquals(errors, ":secondaryMatrixesByName:" + LENGTH_BELOW + "{1}");
    }

    @Test
    public void testMapSecondLevelWithoutValidator() {
        try {
            doTestMapSecondLevel(validator);
            Assert.fail();
        } catch (RuntimeException ex) {
            String format = "No validator added to this annotation! The annotation @%s does not have a validator.";
            Assert.assertEquals(String.format(format, PalindromeString.class.getCanonicalName()), ex.getMessage());
        }
    }

    @Test
    public void testMapSecondLevelWithValidator() {
        doTestMapSecondLevel(Validator.withDefaults().addValidation(PalindromeString.class, new PalindromeString.Validator()));
    }

    public void doTestMapSecondLevel(Validator validator) {
        final float[][] VALID_MATRIX = new float[][] { { 3.7f, 3.5f, 4f, 3.641f }, { 3.7f, 3.7f, 4.0f, 3.622f }, { 4f, 4f, 3.8f, 3.94f } }, INVALID_MATRIX = new float[][] {
            { 2f, 3.5f, 4f, 3.7f },
            { 2f, 3.7f, 4.1f, 3.2f, 3.2f },
            { 4f, 4f, 3.8f, -1f },
            { 4f, 4.5f, 3.6f, 3.7f, 0 } };

        God god = new God("Iluvatar", new Power[] { Power.TELEKINESIS, Power.STRENGTH }, new ArrayList<Weakness>(), VALID_MATRIX,
                new HashMap<String, List<float[][]>>() {
                    private static final long serialVersionUID = -8414198498896964269L;
                    {
                        put("AABBA", new ArrayList<>());
                        put("AABBAA", new ArrayList<float[][]>() {
                            private static final long serialVersionUID = 8898129760388291678L;
                            {
                                add(INVALID_MATRIX);
                                add(VALID_MATRIX);
                            }
                        });
                        put("aBA", new ArrayList<float[][]>() {
                            private static final long serialVersionUID = 8898129760388291678L;
                            {
                                add(VALID_MATRIX);
                                add(VALID_MATRIX);
                            }
                        });
                    }
                });
        List<String> errors = validator.validate(god);
        assertListEquals(errors, new String[] {
            ":secondaryMatrixesByName:[aBA]:" + NOT_PALINDROME,
            ":secondaryMatrixesByName:[AABBA]:" + NOT_PALINDROME,
            ":secondaryMatrixesByName:[AABBA]:" + LENGTH_BELOW + "{1}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:" + LENGTH_DIFFERS + "{3}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:[0]:[0]:" + SMALLER_THAN + "{3.5}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:[1]:" + LENGTH_DIFFERS + "{4}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:[1]:[0]:" + SMALLER_THAN + "{3.5}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:[1]:[2]:" + GREATER_THAN + "{4.0}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:[1]:[3]:" + SMALLER_THAN + "{3.5}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:[1]:[4]:" + SMALLER_THAN + "{3.5}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:[2]:[3]:" + SMALLER_THAN + "{3.5}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:[3]:" + LENGTH_DIFFERS + "{4}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:[3]:[1]:" + GREATER_THAN + "{4.0}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:[3]:[4]:" + SMALLER_THAN + "{3.5}" });
    }
}
