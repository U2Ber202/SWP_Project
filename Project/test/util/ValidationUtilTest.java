package util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import util.ValidationUtil;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import org.junit.jupiter.api.Test;

public class ValidationUtilTest {

    @Test
    public void testNormalize_Null() {
        assertEquals("", ValidationUtil.normalize(null));
    }

    @Test
    public void testNormalize_Empty() {
        assertEquals("", ValidationUtil.normalize(""));
    }

    @Test
    public void testNormalize_Spaces() {
        assertEquals("test", ValidationUtil.normalize("  test  "));
    }

    @Test
    public void testIsBlank_Null() {
        assertTrue(ValidationUtil.isBlank(null));
    }

    @Test
    public void testIsBlank_Empty() {
        assertTrue(ValidationUtil.isBlank(""));
    }

    @Test
    public void testIsBlank_Spaces() {
        assertTrue(ValidationUtil.isBlank("   "));
    }

    @Test
    public void testIsBlank_NotEmpty() {
        assertFalse(ValidationUtil.isBlank("abc"));
    }

    @Test
    public void testIsValidEmail_Valid() {
        assertTrue(ValidationUtil.isValidEmail("test@example.com"));
    }

    @Test
    public void testIsValidEmail_Invalid_NoAt() {
        assertFalse(ValidationUtil.isValidEmail("testexample.com"));
    }

    @Test
    public void testIsValidEmail_Invalid_MultipleAt() {
        assertFalse(ValidationUtil.isValidEmail("test@@example.com"));
    }

    @Test
    public void testIsValidEmail_Invalid_StartWithAt() {
        assertFalse(ValidationUtil.isValidEmail("@example.com"));
    }

    @Test
    public void testIsValidEmail_Invalid_EndWithAt() {
        assertFalse(ValidationUtil.isValidEmail("test@"));
    }

    @Test
    public void testIsValidPhone_Valid() {
        assertTrue(ValidationUtil.isValidPhone("0123456789"));
    }

    @Test
    public void testIsValidPhone_Invalid_Short() {
        assertFalse(ValidationUtil.isValidPhone("012345678"));
    }

    @Test
    public void testIsValidPhone_Invalid_NoLeadingZero() {
        assertFalse(ValidationUtil.isValidPhone("1234567890"));
    }

    @Test
    public void testIsValidPhone_Invalid_Letters() {
        assertFalse(ValidationUtil.isValidPhone("01234abc89"));
    }

    @Test
    public void testIsNonNegativeInteger_Valid() {
        assertTrue(ValidationUtil.isNonNegativeInteger("123"));
        assertTrue(ValidationUtil.isNonNegativeInteger("0"));
    }

    @Test
    public void testIsNonNegativeInteger_Invalid() {
        assertFalse(ValidationUtil.isNonNegativeInteger("-1"));
        assertFalse(ValidationUtil.isNonNegativeInteger("abc"));
    }

    @Test
    public void testIsValidSizeList_Valid() {
        assertTrue(ValidationUtil.isValidSizeList("38,39,40"));
    }

    @Test
    public void testIsValidSizeList_Invalid() {
        assertFalse(ValidationUtil.isValidSizeList("38,abc,40"));
        assertFalse(ValidationUtil.isValidSizeList(""));
        assertFalse(ValidationUtil.isValidSizeList("0,39"));
    }

    @Test
    public void testIsStrongPassword_Valid() {
        assertTrue(ValidationUtil.isStrongPassword("Abc@123"));
    }

    @Test
    public void testIsStrongPassword_Invalid_Short() {
        assertFalse(ValidationUtil.isStrongPassword("Ab@12"));
    }

    @Test
    public void testIsStrongPassword_Invalid_NoSpecial() {
        assertFalse(ValidationUtil.isStrongPassword("Abc1234"));
    }

    @Test
    public void testParsePositiveInt_Valid() {
        assertEquals(Integer.valueOf(10), ValidationUtil.parsePositiveInt("10"));
    }

    @Test
    public void testParsePositiveInt_Invalid() {
        assertNull(ValidationUtil.parsePositiveInt("0"));
        assertNull(ValidationUtil.parsePositiveInt("-5"));
        assertNull(ValidationUtil.parsePositiveInt("abc"));
    }
}
