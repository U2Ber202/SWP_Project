package util;

import jakarta.servlet.http.HttpSession;

public final class PasswordResetUtil {

    public static final String RESET_EMAIL = "resetEmail";
    public static final String RESET_USER = "resetUser";
    public static final String RESET_OTP_HASH = "resetOtpHash";
    public static final String RESET_OTP_EXPIRES_AT = "resetOtpExpiresAt";
    public static final String RESET_OTP_VERIFIED = "resetOtpVerified";
    public static final long OTP_TTL_MILLIS = 10L * 60L * 1000L;

    private PasswordResetUtil() {
    }

    public static boolean isOtpExpired(HttpSession session) {
        Object expiresAt = session.getAttribute(RESET_OTP_EXPIRES_AT);
        if (!(expiresAt instanceof Long)) {
            return true;
        }
        return System.currentTimeMillis() > (Long) expiresAt;
    }

    public static boolean isVerified(HttpSession session) {
        Object verified = session.getAttribute(RESET_OTP_VERIFIED);
        return verified instanceof Boolean && (Boolean) verified && !isOtpExpired(session);
    }

    public static void clear(HttpSession session) {
        session.removeAttribute(RESET_EMAIL);
        session.removeAttribute(RESET_USER);
        session.removeAttribute(RESET_OTP_HASH);
        session.removeAttribute(RESET_OTP_EXPIRES_AT);
        session.removeAttribute(RESET_OTP_VERIFIED);
    }
}
