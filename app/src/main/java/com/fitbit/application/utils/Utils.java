package com.fitbit.application.utils;

import com.google.android.gms.fitness.data.DataPoint;

import java.text.DateFormat;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static String convertDate(DataPoint dataPoint){
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();

        return dateFormat.format(dataPoint.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dataPoint.getStartTime(TimeUnit.MILLISECONDS));
    }
}
