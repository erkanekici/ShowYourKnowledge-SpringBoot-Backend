package com.common.operatorUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtil {

    /** Comparisons */

    public static boolean isGreaterThan (BigDecimal value1, BigDecimal value2) {
        return value1.compareTo(value2) > 0;
    }

    public static boolean isGreaterThanOrEqual (BigDecimal value1, BigDecimal value2) {
        return value1.compareTo(value2) >= 0;
    }

    public static boolean isLessThan (BigDecimal value1, BigDecimal value2) {
        return value1.compareTo(value2) < 0;
    }

    public static boolean isLessThanOrEqual (BigDecimal value1, BigDecimal value2) {
        return value1.compareTo(value2) <= 0;
    }

    public static boolean isEqualsToZero (BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) == 0;
    }

    public static boolean isNotEqualsToZero (BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) != 0;
    }

    public static boolean isGreaterThanZero (BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isLessThanZero (BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) < 0;
    }

    public static boolean isEquals (BigDecimal val1, BigDecimal val2) {
        if (val1 == null && val2 == null) {
            return true;
        } else if (val1 != null && val2 != null) {
            return val1.compareTo(val2) == 0;
        } else {
            return false;
        }
    }

    /** Divide And Round */

    public static BigDecimal divide (BigDecimal dividend, BigDecimal divisor, int scale, RoundingMode roundingMode) {
        return dividend.divide(divisor, scale, roundingMode);
    }

    public static BigDecimal round(BigDecimal bigDecimal, int scale) {
        BigDecimal scaled = bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return scaled;
    }

    /** NullSafe */
    
    public static BigDecimal nullSafeWithDefaultNo(BigDecimal number, BigDecimal defaultNumber) {
    	return number == null ? defaultNumber : number;
    }
    
    public static BigDecimal nullSafeZero(BigDecimal number) {
    	return BigDecimalUtil.nullSafeWithDefaultNo(number, BigDecimal.ZERO);
    }
    
    public static BigDecimal nullSafeOne(BigDecimal number) {
    	return BigDecimalUtil.nullSafeWithDefaultNo(number, BigDecimal.ONE);
    }

}
