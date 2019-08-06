package com.example.absentoffice;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ExtraUtil {

    public static String getCurrYear() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        String yearCurrent = yearFormat.format(calendar.getTime());
        return yearCurrent;
    }
    public static String getCurrMonth() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
        String monthCurrent = monthFormat.format(calendar.getTime());
        return monthCurrent;
    }
    public static String getCurrDay() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        String dayCurrent = dayFormat.format(calendar.getTime());
        return dayCurrent;
    }
}
