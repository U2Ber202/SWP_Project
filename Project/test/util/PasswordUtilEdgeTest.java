package util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import util.PasswordUtil;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import org.junit.jupiter.api.Test;

public class PasswordUtilEdgeTest {

    @Test
    public void testMatches_NullInputs() {
        assertFalse(PasswordUtil.matches(null, null));
        assertFalse(PasswordUtil.matches("pass", null));
        assertFalse(PasswordUtil.matches(null, "hash"));
    }

    @Test
    public void testMatches_WrongIterationCount() {
        String hash = "pbkdf2$1$salt$hash"; // Only 1 iteration
        // This might fail if the implementation expects a specific range or if the decode fails
        assertFalse(PasswordUtil.matches("password", hash));
    }

    @Test
    public void testMatches_InvalidBase64Salt() {
        assertFalse(PasswordUtil.matches("password", "pbkdf2$65536$invalid_base64$hash"));
    }

    @Test
    public void testMatches_InvalidBase64Hash() {
        assertFalse(PasswordUtil.matches("password", "pbkdf2$65536$salt$invalid_base64"));
    }

    @Test
    public void testMatches_IncorrectPartCount() {
        assertFalse(PasswordUtil.matches("password", "pbkdf2$65536$salt"));
    }

    @Test
    public void testNeedsRehash_Null() {
        assertTrue(PasswordUtil.needsRehash(null));
    }

    @Test
    public void testNeedsRehash_Empty() {
        assertTrue(PasswordUtil.needsRehash(""));
    }

    @Test
    public void testHash_EmptyString() {
        String hashed = PasswordUtil.hash("");
        assertTrue(PasswordUtil.matches("", hashed));
    }

    @Test
    public void testMatches_CaseSensitivity() {
        String hashed = PasswordUtil.hash("Password123");
        assertFalse(PasswordUtil.matches("password123", hashed));
    }

    @Test
    public void testHash_LengthCheck() {
        String hashed = PasswordUtil.hash("a");
        assertTrue(PasswordUtil.matches("a", hashed));
    }
}
