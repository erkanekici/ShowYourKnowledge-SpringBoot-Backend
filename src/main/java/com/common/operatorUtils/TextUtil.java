package com.common.operatorUtils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class TextUtil {

    private static TextUtil _this = null;

    public static TextUtil getInstance() {
        if (_this == null)
            _this = new TextUtil();
        return _this;
    }

    public static String subString(String text, int endIndex) {
        return subString(text, 0, endIndex);
    }

    public static String subString(String text, int beginIndex, int endIndex) {
        if (text != null)
            return text.substring(beginIndex, Math.min(endIndex, text.length()));
        else
            return null;
    }

    public String substringBeforeLast(final String str, final String separator) {
        if (isTextEmpty(str) || isTextEmpty(separator)) {
            return str;
        }
        final int pos = str.lastIndexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    public boolean isTextEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNullOrEmpty(String s) {
        if (s == null) {
            return true;
        }
        return s.isEmpty();
    }

    public static boolean isNullOrEmptyAfterTrim(String s) {
        if (s == null) {
            return true;
        }
        return s.trim().isEmpty();
    }

    public static String getDefault(Object val) {
        return getDefault(val, "");
    }

    public static String getDefault(Object val, String defaultVal) {
        if (val == null)
            return defaultVal;
        else
            return val.toString();
    }

    public static String convertBigDecimalToStringWithDefaultValue(BigDecimal bd, String defaultValue) {
        if (bd != null) {
            return String.valueOf(bd);
        } else {
            return defaultValue;
        }
    }

    public static boolean isEquals(String val1, String val2) {
        return StringUtils.equals(val1, val2);
    }

    public static String timeToString(long timeMillis) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return dateFormat.format(new Date(timeMillis));
    }

    public static boolean isNumeric(String str) {
        String regex = "[0-9]+[\\.]?[0-9]*";
        return Pattern.matches(regex, str);
    }
}
