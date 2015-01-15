package xyz.luan.sabv;

import static xyz.luan.sabv.TestCommons.assertListEmpty;
import static xyz.luan.sabv.TestCommons.assertListEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import xyz.luan.sabv.customs.PalindromeString;
import xyz.luan.sabv.entities.God;
import xyz.luan.sabv.entities.Power;
import xyz.luan.sabv.entities.Weakness;

public class ArraysTest extends BaseTestCase {

    @Test
    public void testArraysFirstLevelAnnotationsInvalid() {
        God god = new God("Poseidon", null, null, null, null);
        List<String> errors = ValidationHelper.validate(god);
        assertListEquals(errors, ":powers:Required.empty", ":weaknesses:Required.empty", ":calculationMatrix:Required.empty");
    }

    @Test
    public void testArraysSecondLevelAnnotationsInvalid() {
        God god = new God("Poseidon", new Power[] { null }, new ArrayList<Weakness>() {
            private static final long serialVersionUID = -7219674458766897456L;
            {
                add(Weakness.CATS);
                add(null);
                add(Weakness.KRIPTONITE);
            }
        }, null, null);
        List<String> errors = ValidationHelper.validate(god);
        assertListEquals(errors, new String[] {
            ":powers:Array.lengthBelow{2}",
            ":powers:[0]:Required.empty",
            ":weaknesses:Array.lengthAbove{2}",
            ":weaknesses:[1]:Required.empty",
            ":calculationMatrix:Required.empty"
        });
    }

    @Test
    public void testArraysThirdLevelAnnotationsInvalid() {
        God god = new God(null, new Power[] { Power.FLIGHT, Power.IMMORTALITY }, new ArrayList<Weakness>(), new float[][] {
            {2f, 3.5f, 4f, 3.7f},
            {2f, 3.7f, 4.1f, 3.2f, 3.2f},
            {4f, 4f, 3.8f, -1f},
            {4f, 4.5f, 3.6f, 3.7f, 0}
        }, null);
        List<String> errors = ValidationHelper.validate(god);
        assertListEquals(errors, new String[] {
            ":calculationMatrix:Array.lengthDiffers{3}",
            ":calculationMatrix:[0]:[0]:Numeric.smallerThan{3.5}",
            ":calculationMatrix:[1]:Array.lengthDiffers{4}",
            ":calculationMatrix:[1]:[0]:Numeric.smallerThan{3.5}",
            ":calculationMatrix:[1]:[2]:Numeric.greaterThan{4.0}",
            ":calculationMatrix:[1]:[3]:Numeric.smallerThan{3.5}",
            ":calculationMatrix:[1]:[4]:Numeric.smallerThan{3.5}",
            ":calculationMatrix:[2]:[3]:Numeric.smallerThan{3.5}",
            ":calculationMatrix:[3]:Array.lengthDiffers{4}",
            ":calculationMatrix:[3]:[1]:Numeric.greaterThan{4.0}",
            ":calculationMatrix:[3]:[4]:Numeric.smallerThan{3.5}"
        });
    }

    @Test
    public void testArraysThirdLevelAnnotationsValid() {
        God god = new God(null, new Power[] { Power.FLIGHT, Power.IMMORTALITY }, new ArrayList<Weakness>(), new float[][] {
            {3.7f, 3.5f, 4f, 3.641f},
            {3.7f, 3.7f, 4.0f, 3.622f},
            {4f, 4f, 3.8f, 3.94f}
        }, null);
        List<String> errors = ValidationHelper.validate(god);
        assertListEmpty(errors);
    }

    @Test
    public void testMapFirstLevel() {
        God god = new God(null, new Power[] { Power.FLIGHT, Power.IMMORTALITY }, new ArrayList<Weakness>(), new float[][] {
            {3.7f, 3.5f, 4f, 3.641f},
            {3.7f, 3.7f, 4.0f, 3.622f},
            {4f, 4f, 3.8f, 3.94f}
        }, new HashMap<>());
        List<String> errors = ValidationHelper.validate(god);
        assertListEquals(errors, ":secondaryMatrixesByName:Array.lengthBelow{1}");
    }

    @Test
    public void testMapSecondLevelWithoutValidator() {
        ValidationHelper.VALIDATORS.remove(PalindromeString.class);
        try {
            doTestMapSecondLevel();
            Assert.fail();
        } catch (RuntimeException ex) {
            Assert.assertEquals("No validator added to this annotation! The annotation @xyz.luan.sabv.customs.PalindromeString() does not have a validator.", ex.getMessage());
        }
    }

    @Test
    public void testMapSecondLevelWithValidator() {
        ValidationHelper.VALIDATORS.put(PalindromeString.class, new PalindromeString.Validator());
        doTestMapSecondLevel();
    }

    public void doTestMapSecondLevel() {
        final float [][] VALID_MATRIX = new float[][] {
            {3.7f, 3.5f, 4f, 3.641f},
            {3.7f, 3.7f, 4.0f, 3.622f},
            {4f, 4f, 3.8f, 3.94f}
        }, INVALID_MATRIX = new float[][] {
            {2f, 3.5f, 4f, 3.7f},
            {2f, 3.7f, 4.1f, 3.2f, 3.2f},
            {4f, 4f, 3.8f, -1f},
            {4f, 4.5f, 3.6f, 3.7f, 0}
        };

        God god = new God("Iluvatar", new Power[] { Power.TELEKINESIS, Power.STRENGTH }, new ArrayList<Weakness>(), VALID_MATRIX, new HashMap<String, List<float[][]>>(){
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
        List<String> errors = ValidationHelper.validate(god);
        assertListEquals(errors, new String[] {
            ":secondaryMatrixesByName:[aBA]:xyz.luan.sabv.customs.PalindromeString.notPalindrome",
            ":secondaryMatrixesByName:[AABBA]:xyz.luan.sabv.customs.PalindromeString.notPalindrome",
            ":secondaryMatrixesByName:[AABBA]:Array.lengthBelow{1}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:Array.lengthDiffers{3}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:[0]:[0]:Numeric.smallerThan{3.5}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:[1]:Array.lengthDiffers{4}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:[1]:[0]:Numeric.smallerThan{3.5}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:[1]:[2]:Numeric.greaterThan{4.0}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:[1]:[3]:Numeric.smallerThan{3.5}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:[1]:[4]:Numeric.smallerThan{3.5}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:[2]:[3]:Numeric.smallerThan{3.5}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:[3]:Array.lengthDiffers{4}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:[3]:[1]:Numeric.greaterThan{4.0}",
            ":secondaryMatrixesByName:[AABBAA]:[0]:[3]:[4]:Numeric.smallerThan{3.5}"
        });
    }
}
