package com.common.operatorUtils;

import java.math.BigDecimal;

public class LongUtil {

    /** Comparisons */

    public static boolean isGreaterThan (Long value1, Long value2) {
        return value1.compareTo(value2) > 0;
    }

    public static boolean isLessThan (Long value1, Long value2) {
        return value1.compareTo(value2) < 0;
    }

    public static boolean isEquals(Long val1, Long val2) {
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

    public static Long nullSafeWithDefaultNo(Long val, Long defaultVal) {
        return val == null ? defaultVal : val;
    }

    public static Long nullSafeZero(Long number) {
        return nullSafeWithDefaultNo(number, 0L);
    }

    public static Long nullSafeOne(Long number) {
        return nullSafeWithDefaultNo(number, 1L);
    }

}
