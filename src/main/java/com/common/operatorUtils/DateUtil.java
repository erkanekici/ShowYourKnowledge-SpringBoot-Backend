package com.common.operatorUtils;

import com.common.DayCountOfYear;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public final class DateUtil {

    private static DateUtil _this = null;

    public static DateUtil getInstance() {
        if (_this == null)
            _this = new DateUtil();
        return _this;
    }

    private static ZoneId getZoneId() {
        return ZoneId.of(getZoneName());
    }

    private static String getZoneName() {
        return "Europe/Istanbul";
    }

    public static Long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static boolean isGreaterThan (LocalDate value1,LocalDate value2) {
        return value1.compareTo(value2) > 0;
    }

    public static boolean isLessThan (LocalDate value1,LocalDate value2) {
        return value1.compareTo(value2) < 0;
    }

    public static boolean isEquals(LocalDate val1, LocalDate val2) {
        if (val1 == null && val2 == null) {
            return true;
        } else if (val1 != null && val2 != null) {
            return val1.compareTo(val2)==0;
        } else {
            return false;
        }
    }

    public static Long between(LocalDate value1, LocalDate value2, ChronoUnit chronoUnit) {
        Long amountOfTime = chronoUnit.between(value1, value2);
        return amountOfTime;
    }

    public static Integer getRemainingDays(LocalDate expireDate) {
        if(expireDate == null) {
            return null;
        }
        return between(LocalDate.now(), expireDate, ChronoUnit.DAYS).intValue() + 1;
    }

    public boolean isLeapYear(int year) { //Artık yıl, Miladî takvimde (Gregoryen takvim) 365 yerine 366 günü olan yıldır Örn: 2020
        return (((year % 4) == 0) && ((year % 100) != 0) || ((year % 400) == 0));
    }

    /** CONVERSIONS */

    public static String convertLocalDateToString(LocalDate date, String datePattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        return date.format(formatter);
    }

    public static String convertOffsetDateTimeToString(OffsetDateTime dateTime, String dateTimePattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);
        return dateTime.format(formatter);
    }

    public static LocalDate convertStringToLocalDate(String date, String datePattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }

    public static OffsetDateTime convertStringToOffsetDateTimeWithZone(String date, String datePattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern).withZone(getZoneId());
        ZonedDateTime datetime = ZonedDateTime.parse(date, formatter);
        return datetime.toOffsetDateTime();
    }

    public static LocalTime convertStringToLocalTime(String time) {
        LocalTime localTime = LocalTime.parse(time);
        return localTime;
    }

    public static LocalDate convertDateToLocalDateWithZone(java.util.Date date){ return date.toInstant().atZone(getZoneId()).toLocalDate(); }

    public static OffsetDateTime convertLocalDateTimeToOffsetDateTimeWithZone(LocalDateTime localDateTime){
        ZoneOffset zoneOffSet = getZoneId().getRules().getOffset(localDateTime);
        OffsetDateTime offsetDateTime = localDateTime.atOffset(zoneOffSet);
        return offsetDateTime;
    }

    /** DATE DIFF 360 */

    public double dateDiff360(LocalDate startDate, LocalDate endDate, boolean methodUS) {
        int startDay = startDate.getDayOfMonth();
        int startMonth = startDate.getMonthValue();
        int startYear = startDate.getYear();
        int endDay = endDate.getDayOfMonth();
        int endMonth = endDate.getMonthValue();
        int endYear = endDate.getYear();

        return dateDiff360(startDay, startMonth, startYear, endDay, endMonth, endYear, methodUS);
    }

    public double dateDiff360(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear, boolean methodUS) {
        if (startDay == 31) {
            --startDay;
        } else if (methodUS && startMonth == 2 && (startDay == 29 || (startDay == 28 && !isLeapYear(startYear)))) {
            startDay = 30;
        }
        if (endDay == 31) {
            if (methodUS && startDay != 30) {
                endDay = 1;
                if (endMonth == 12) {
                    ++endYear;
                    endMonth = 1;
                } else {
                    ++endMonth;
                }
            } else {
                endDay = 30;
            }
        }
        return endDay + endMonth * 30 + endYear * DayCountOfYear.D_360.getInt() - startDay - startMonth * 30 - startYear * 360;
    }
}