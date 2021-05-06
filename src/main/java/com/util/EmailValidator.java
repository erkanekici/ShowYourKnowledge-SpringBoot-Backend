package com.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

    public static boolean isValidEmailAddress(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
