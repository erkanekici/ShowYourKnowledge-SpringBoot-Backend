package com.common;

public class TextUtil {

    private static TextUtil _this = null;

    public static TextUtil getInstance() {
        if (_this == null)
            _this = new TextUtil();
        return _this;
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
}
