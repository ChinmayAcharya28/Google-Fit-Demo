package com.fitbit.application.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.fitness.data.DataPoint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean compareDate(DataPoint dataPoint){
        Date start = new Date(dataPoint.getStartTime(TimeUnit.MILLISECONDS));
        Date end = new Date(dataPoint.getEndTime(TimeUnit.MILLISECONDS));

        LocalDateTime localDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        int year  = localDate.getYear();
        int month = localDate.getMonthValue();
        int day   = localDate.getDayOfMonth();
        int hrs   = localDate.getHour();
        int min   = localDate.getMinute();

        LocalDateTime localDate1 = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        int year1  = localDate1.getYear();
        int month1 = localDate1.getMonthValue();
        int day1   = localDate1.getDayOfMonth();
        int hrs1   = localDate1.getHour();
        int min1   = localDate1.getMinute();

        if (year == year1 && month == month1 && day == day1) {
            if ((hrs == 0 && min > 15) && (hrs1 == 0 && min1 > 15)) {
                return true;
            }else if ((hrs == 23 && min < 45) && (hrs1 == 23 && min1 < 45)){
                return true;
            }else if((hrs > 0 && hrs1 > 0) || (hrs < 23 && hrs1 < 23)){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}
