package com.util;

import com.model.DayCountOfYear;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateUtil {

    private DateUtil() {
    }

    public static double dateDiff360(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear, boolean methodUS) {
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

    public static double dateDiff360(LocalDate startDate, LocalDate endDate, boolean methodUS) {
        int startDay = startDate.getDayOfMonth();
        int startMonth = startDate.getMonthValue();
        int startYear = startDate.getYear();
        int endDay = endDate.getDayOfMonth();
        int endMonth = endDate.getMonthValue();
        int endYear = endDate.getYear();

        return dateDiff360(startDay, startMonth, startYear, endDay, endMonth, endYear, methodUS);
    }

    public static boolean isLeapYear(int year) {
        return (((year % 4) == 0) && ((year % 100) != 0) || ((year % 400) == 0));
    }

    public static boolean isValidDate(String date) {
        DateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static String convertToStiring(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        return date.format(formatter);
    }

}
