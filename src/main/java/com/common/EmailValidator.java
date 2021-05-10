package com.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

    private static EmailValidator _this = null;

    public static EmailValidator getInstance() {
        if (_this == null)
            _this = new EmailValidator();
        return _this;
    }

    public static boolean isValidEmailAddress(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
