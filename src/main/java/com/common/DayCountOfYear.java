package com.common;

import java.math.BigDecimal;

public enum DayCountOfYear {

    D_365(365),
    D_360(360);

    private int dayCount;


    private DayCountOfYear(int dayCount) {
        this.dayCount = dayCount;
    }

    public int getInt() {
        return dayCount;
    }

    public long getLong() {
        return (long) dayCount;
    }

    public BigDecimal getBigDecimal() {
        return new BigDecimal(dayCount);
    }

    public double getDouble() {
        return (double) dayCount;
    }


}
