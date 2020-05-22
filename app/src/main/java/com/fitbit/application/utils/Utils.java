package com.fitbit.application.utils;

import com.google.android.gms.fitness.data.DataPoint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static String DATE_FORMAT = "dd/MM/yy";

    public static String convertStartDate(DataPoint dataPoint){
        Date date=new Date(dataPoint.getStartTime(TimeUnit.MILLISECONDS));
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        return dateFormat.format(date);
    }

    public static boolean isToday(DataPoint dataPoint){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        return (dateFormat.format(date).equalsIgnoreCase(convertStartDate(dataPoint)));

    }

    public static String formatToYesterdayOrToday(String date) {
        Date dateTime = null;
        try {
            dateTime = new SimpleDateFormat(DATE_FORMAT).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);


        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return "Today";
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "Yesterday";
        } else {
            return date;
        }
    }

    public static String convertEndDate(DataPoint dataPoint){
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();

        return dateFormat.format(dataPoint.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dataPoint.getEndTime(TimeUnit.MILLISECONDS));
    }
}
