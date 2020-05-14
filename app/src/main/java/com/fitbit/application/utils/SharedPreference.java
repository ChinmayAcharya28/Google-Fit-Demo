package com.fitbit.application.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {

    public static SharedPreferences get(Context context) {

        return context.getSharedPreferences("FITBIT", 0);
    }

    public static void setFirstTimeLoggedIn(Context context, boolean isLoggedIn) {
        SharedPreferences prefs = get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoogedIn", isLoggedIn);
        editor.apply();
    }

    public static boolean getFirstTimeLoggedIn(Context context) {
        SharedPreferences prefs = get(context);
        return prefs.getBoolean("isLoogedIn", false);
    }

}
