package com.fitbit.application.history.repository;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.fitbit.application.MainActivity;
import com.fitbit.application.utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HistoryFetchTask extends AsyncTask<Void, ArrayList<DataReadResponse>, ArrayList<DataReadResponse>> {

    private Context mContext;
    private IHistoryCallback iHistoryCallback;
    ArrayList<DataReadResponse> dataReadResponses = new ArrayList<>();

    public HistoryFetchTask(Context context, IHistoryCallback iHistoryCallback){
        this.mContext = context;
        this.iHistoryCallback = iHistoryCallback;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected ArrayList<DataReadResponse> doInBackground(Void... voids) {
        DataReadResponse dataReadResponse = null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        //long endTime = cal.getTimeInMillis();
        //long endTime = 1589740200000L;
        long test = cal.getTimeInMillis();

        Calendar newCalendar = Calendar.getInstance();;
        newCalendar.setTime(new Date());
        newCalendar.set(Calendar.HOUR_OF_DAY, 0);
        newCalendar.set(Calendar.MINUTE, 0);
        newCalendar.set(Calendar.SECOND, 0);
        newCalendar.set(Calendar.MILLISECOND, 0);
        newCalendar.add(Calendar.WEEK_OF_YEAR, -2);

        long test1 = newCalendar.getTimeInMillis();
        //long startTime = cal.getTimeInMillis();
        //long startTime = 1589653800000L;



        LocalDateTime end = Utils.toLocalDateTime(cal);
        LocalDateTime start = end.minus(14, ChronoUnit.DAYS);


        for (LocalDateTime date = start; date.isBefore(end); date = date.plusDays(1)) {

            long endTime = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            LocalDateTime value = date.minus(24, ChronoUnit.HOURS);
            long startTime = value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

            System.out.println("------------------------- = " +startTime);
            System.out.println("------------------------- = " +endTime);

            GoogleSignInAccount mGoogleSignInAccount = GoogleSignIn.getLastSignedInAccount(mContext);
            if (mGoogleSignInAccount == null) {
                mGoogleSignInAccount = MainActivity.getClient();
            }

            Task<DataReadResponse> response = Fitness.getHistoryClient((Activity) mContext, mGoogleSignInAccount)
                    .readData(new DataReadRequest.Builder()
                            .read(DataType.TYPE_STEP_COUNT_DELTA)
                            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                            .build());

            try {
                dataReadResponse = Tasks.await(response, 30, TimeUnit.SECONDS);
                dataReadResponses.add(dataReadResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dataReadResponses;
    }

    @Override
    protected void onPostExecute(ArrayList<DataReadResponse> dataReadResponse) {
        super.onPostExecute(dataReadResponse);

        iHistoryCallback.onComplete(dataReadResponse);
    }
}
