package com.fitbit.application.utils;

import com.google.android.gms.fitness.data.DataPoint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static String convertStartDate(DataPoint dataPoint){
        Date date=new Date(dataPoint.getStartTime(TimeUnit.MILLISECONDS));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

        return dateFormat.format(date);
    }

    public static String convertEndDate(DataPoint dataPoint){
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();

        return dateFormat.format(dataPoint.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dataPoint.getEndTime(TimeUnit.MILLISECONDS));
    }
}
