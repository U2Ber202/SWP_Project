package util;

public final class ValidationUtil {

    private ValidationUtil() {
    }

    public static String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    public static boolean isBlank(String value) {
        return normalize(value).isEmpty();
    }

    public static boolean isValidEmail(String email) {
        String normalizedEmail = normalize(email);
        return !normalizedEmail.isEmpty()
                && normalizedEmail.contains("@")
                && normalizedEmail.indexOf('@') > 0
                && normalizedEmail.indexOf('@') == normalizedEmail.lastIndexOf('@')
                && normalizedEmail.indexOf('@') < normalizedEmail.length() - 1;
    }

    public static boolean isValidPhone(String phone) {
        return normalize(phone).matches("0\\d{9}");
    }

    public static boolean isNonNegativeInteger(String value) {
        String normalizedValue = normalize(value);
        if (!normalizedValue.matches("\\d+")) {
            return false;
        }
        try {
            return Integer.parseInt(normalizedValue) >= 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isValidSize(String size) {
        String normalizedSize = normalize(size);
        if (normalizedSize.isEmpty()) {
            return false;
        }
        if (normalizedSize.matches("-?\\d+")) {
            try {
                return Integer.parseInt(normalizedSize) >= 0;
            } catch (NumberFormatException ex) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidSizeList(String sizeList) {
        String normalizedSizeList = normalize(sizeList);
        if (normalizedSizeList.isEmpty()) {
            return false;
        }

        String[] parts = normalizedSizeList.split(",");
        boolean hasSize = false;
        for (String part : parts) {
            String normalizedPart = normalize(part);
            if (normalizedPart.isEmpty() || !normalizedPart.matches("\\d+")) {
                return false;
            }
            try {
                int size = Integer.parseInt(normalizedPart);
                if (size <= 0) {
                    return false;
                }
            } catch (NumberFormatException ex) {
                return false;
            }
            hasSize = true;
        }
        return hasSize;
    }

    public static boolean isStrongPassword(String password) {
        String normalizedPassword = normalize(password);
        return normalizedPassword.length() >= 6
                && normalizedPassword.matches(".*[a-z].*")
                && normalizedPassword.matches(".*[A-Z].*")
                && normalizedPassword.matches(".*\\d.*")
                && normalizedPassword.matches(".*[^A-Za-z0-9].*");
    }

    public static Integer parsePositiveInt(String value) {
        String normalizedValue = normalize(value);
        if (!normalizedValue.matches("\\d+")) {
            return null;
        }
        try {
            int parsed = Integer.parseInt(normalizedValue);
            return parsed > 0 ? parsed : null;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static Integer parseNonNegativeInt(String value) {
        String normalizedValue = normalize(value);
        if (!normalizedValue.matches("\\d+")) {
            return null;
        }
        try {
            return Integer.parseInt(normalizedValue);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
