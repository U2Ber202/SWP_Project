package util;

import org.junit.Test;
import static org.junit.Assert.*;

public class PasswordUtilTest {

    @Test
    public void testHash_NotNull() {
        String password = "password123";
        String hashed = PasswordUtil.hash(password);
        assertNotNull(hashed);
        assertTrue(hashed.startsWith("pbkdf2$"));
    }

    @Test
    public void testHash_DifferentSalts() {
        String password = "password123";
        String hash1 = PasswordUtil.hash(password);
        String hash2 = PasswordUtil.hash(password);
        assertNotEquals(hash1, hash2, "Hashes of the same password should be different due to random salt");
    }

    @Test
    public void testMatches_CorrectPassword() {
        String password = "StrongPassword@123";
        String hashed = PasswordUtil.hash(password);
        assertTrue(PasswordUtil.matches(password, hashed));
    }

    @Test
    public void testMatches_WrongPassword() {
        String password = "StrongPassword@123";
        String hashed = PasswordUtil.hash(password);
        assertFalse(PasswordUtil.matches("WrongPassword@123", hashed));
    }

    @Test
    public void testMatches_EmptyPassword() {
        String hashed = PasswordUtil.hash("password");
        assertFalse(PasswordUtil.matches("", hashed));
        assertFalse(PasswordUtil.matches(null, hashed));
    }

    @Test
    public void testMatches_LegacyPassword() {
        // Test compatibility with plain text passwords if they exist in DB
        assertTrue(PasswordUtil.matches("plain", "plain"));
        assertFalse(PasswordUtil.matches("plain", "wrong"));
    }

    @Test
    public void testNeedsRehash_Legacy() {
        assertTrue(PasswordUtil.needsRehash("plain_text_password"));
    }

    @Test
    public void testNeedsRehash_Hashed() {
        String hashed = PasswordUtil.hash("password");
        assertFalse(PasswordUtil.needsRehash(hashed));
    }

    @Test
    public void testMatches_InvalidHashFormat() {
        assertFalse(PasswordUtil.matches("password", "pbkdf2$65536$invalid$format"));
    }
    
    @Test
    public void testMatches_NullStoredPassword() {
        assertFalse(PasswordUtil.matches("password", null));
    }
}
