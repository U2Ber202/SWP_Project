package util;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public final class AppConfig {

    private AppConfig() {
    }

    public static String get(String key, String defaultValue) {
        String value = getFromJndi(key);
        if (!ValidationUtil.isBlank(value)) {
            return value;
        }

        value = System.getProperty(key);
        if (!ValidationUtil.isBlank(value)) {
            return value;
        }

        value = System.getenv(key);
        if (!ValidationUtil.isBlank(value)) {
            return value;
        }

        return defaultValue;
    }

    public static String getRequired(String key, String defaultValue) {
        String value = get(key, defaultValue);
        if (ValidationUtil.isBlank(value) || "CHANGE_ME".equalsIgnoreCase(value)) {
            throw new IllegalStateException("Missing required config: " + key);
        }
        return value;
    }

    private static String getFromJndi(String key) {
        try {
            InitialContext context = new InitialContext();
            Object value = context.lookup("java:comp/env/" + key);
            return value == null ? null : value.toString();
        } catch (NamingException ex) {
            return null;
        }
    }
}
