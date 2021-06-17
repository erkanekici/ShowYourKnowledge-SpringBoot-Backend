package com.common.operatorUtils;

import java.math.BigDecimal;

public class IntegerUtil {

    /** Comparisons */

    public static boolean isGreaterThan (Integer value1,Integer value2) {
        return value1.compareTo(value2) > 0;
    }

    public static boolean isLessThan (Integer value1,Integer value2) {
        return value1.compareTo(value2) < 0;
    }

    public static boolean isEquals(Integer val1, Integer val2) {
        if (val1 == null && val2 == null) {
            return true;
        } else if (val1 != null && val2 != null) {
            return val1.compareTo(val2)==0;
        } else {
            return false;
        }
    }

    /** Conversions */

    /** NullSafe */

    public static Integer nullSafeWithDefaultNo(Integer val, Integer defaultVal) {
        return val == null ? defaultVal : val;
    }

    public static Integer nullSafeZero(Integer number) {
        return nullSafeWithDefaultNo(number, 0);
    }

    public static Integer nullSafeOne(Integer number) {
        return nullSafeWithDefaultNo(number, 1);
    }

}
