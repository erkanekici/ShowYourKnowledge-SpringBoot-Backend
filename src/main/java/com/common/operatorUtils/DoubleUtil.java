package com.common.operatorUtils;

import java.math.BigDecimal;

public class DoubleUtil {

    /** Comparisons */

    public static boolean isGreaterThan (Double value1,Double value2) {
        return value1.compareTo(value2) > 0;
    }

    public static boolean isLessThan (Double value1,Double value2) {
        return value1.compareTo(value2) < 0;
    }

    public static boolean isEquals(Double val1, Double val2) {
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

    public static Double nullSafeWithDefaultNo(Double val, Double defaultVal) {
        return val == null ? defaultVal : val;
    }

    public static Double nullSafeZero(Double number) {
        return nullSafeWithDefaultNo(number, 0d);
    }

    public static Double nullSafeOne(Double number) {
        return nullSafeWithDefaultNo(number, 1d);
    }

}
