package util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import util.ValidationUtil;
//import org.junit.jupiter.api.Test;

public class ValidationUtilEdgeTest {

    @Test
    public void testNormalize_WithTabsAndNewlines() {
        assertEquals("test", ValidationUtil.normalize(" \t\n test \r "));
    }

    @Test
    public void testIsValidEmail_NoDomain() {
        assertFalse(ValidationUtil.isValidEmail("test@"));
    }

    @Test
    public void testIsValidEmail_NoUser() {
        assertFalse(ValidationUtil.isValidEmail("@example.com"));
    }

    @Test
    public void testIsValidPhone_WrongLength() {
        assertFalse(ValidationUtil.isValidPhone("01234567890")); // 11 digits
        assertFalse(ValidationUtil.isValidPhone("012345678"));  // 9 digits
    }

    @Test
    public void testIsNonNegativeInteger_LargeValue() {
        assertTrue(ValidationUtil.isNonNegativeInteger("2147483647"));
    }

    @Test
    public void testIsNonNegativeInteger_Overflow() {
        assertFalse(ValidationUtil.isNonNegativeInteger("2147483648")); // Exceeds Integer.MAX_VALUE
    }

    @Test
    public void testIsValidSizeList_TrailingComma() {
        assertFalse(ValidationUtil.isValidSizeList("38,39,"));
    }

    @Test
    public void testIsValidSizeList_LeadingComma() {
        assertFalse(ValidationUtil.isValidSizeList(",38,39"));
    }

    @Test
    public void testParsePositiveInt_ZeroAsString() {
        assertNull(ValidationUtil.parsePositiveInt("0"));
    }

    @Test
    public void testParseNonNegativeInt_Negative() {
        assertNull(ValidationUtil.parseNonNegativeInt("-1"));
    }
}
