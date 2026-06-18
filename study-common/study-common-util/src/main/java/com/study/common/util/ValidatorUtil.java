package com.study.common.util;

import java.util.regex.Pattern;

public final class ValidatorUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$");
    private static final Pattern URL_PATTERN = Pattern.compile("^https?://[a-zA-Z0-9.-]+(?:/[^\\s]*)?$");
    private static final Pattern IPV4_PATTERN = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

    private ValidatorUtil() {
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isEmail(String email) {
        return isNotEmpty(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isPhone(String phone) {
        return isNotEmpty(phone) && PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isIdCard(String idCard) {
        return isNotEmpty(idCard) && ID_CARD_PATTERN.matcher(idCard).matches();
    }

    public static boolean isUrl(String url) {
        return isNotEmpty(url) && URL_PATTERN.matcher(url).matches();
    }

    public static boolean isIpv4(String ip) {
        return isNotEmpty(ip) && IPV4_PATTERN.matcher(ip).matches();
    }

    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPositiveInteger(String str) {
        if (isEmpty(str)) {
            return false;
        }
        if (!isNumeric(str)) {
            return false;
        }
        long value = Long.parseLong(str);
        return value > 0;
    }

    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    public static boolean isInRange(long value, long min, long max) {
        return value >= min && value <= max;
    }

    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    public static boolean lengthInRange(String str, int min, int max) {
        if (str == null) {
            return false;
        }
        int len = str.length();
        return len >= min && len <= max;
    }

    public static boolean hasChinese(String str) {
        if (isEmpty(str)) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (Character.UnicodeScript.of(c) == Character.UnicodeScript.HAN) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsSpecialChar(String str) {
        if (isEmpty(str)) {
            return false;
        }
        String specialChars = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
        for (char c : str.toCharArray()) {
            if (specialChars.indexOf(c) >= 0) {
                return true;
            }
        }
        return false;
    }

    public static void requireNotEmpty(String str, String fieldName) {
        if (isEmpty(str)) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
    }

    public static void requireEmail(String email, String fieldName) {
        if (!isEmail(email)) {
            throw new IllegalArgumentException(fieldName + " is not a valid email");
        }
    }

    public static void requirePhone(String phone, String fieldName) {
        if (!isPhone(phone)) {
            throw new IllegalArgumentException(fieldName + " is not a valid phone number");
        }
    }
}