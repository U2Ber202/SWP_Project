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

    private static final String[] BAD_WORDS = {
        // Vietnamese vulgarities
        "dm", "vcl", "cl", "dcm", "cho", "ngu", "cac", "lon", "buoi", "du", "dit", "me", "ba", "ma",
        "khon", "nan", "mat", "day", "oc", "cho", "do", "dien", "khung", "di", "diem",
        // English vulgarities
        "fuck", "shit", "bitch", "ass", "bastard", "dick", "pussy", "fucker", "shitty", "hell", "damn"
    };

    public static boolean isValidEmail(String email) {
        String normalizedEmail = normalize(email);
        return normalizedEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean isValidPhone(String phone) {
        return normalize(phone).matches("^0[35789]\\d{8}$");
    }

    public static boolean isValidLength(String value, int min, int max) {
        String normalized = normalize(value);
        return normalized.length() >= min && normalized.length() <= max;
    }

    public static String filterBadWords(String content) {
        if (isBlank(content)) {
            return content;
        }
        String filtered = content;
        for (String word : BAD_WORDS) {
            // Case insensitive replacement with asterisks
            filtered = filtered.replaceAll("(?i)\\b" + word + "\\b", "***");
        }
        return filtered;
    }

    public static boolean hasBadWords(String content) {
        if (isBlank(content)) {
            return false;
        }
        String lowerContent = content.toLowerCase();
        for (String word : BAD_WORDS) {
            if (lowerContent.matches(".*\\b" + word + "\\b.*")) {
                return true;
            }
        }
        return false;
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
