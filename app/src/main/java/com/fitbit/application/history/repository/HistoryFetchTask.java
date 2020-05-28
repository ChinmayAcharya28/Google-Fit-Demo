package com.fitbit.application.history.repository;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.fitbit.application.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HistoryFetchTask extends AsyncTask<Void, DataReadResponse, DataReadResponse> {

    private Context mContext;
    private IHistoryCallback iHistoryCallback;

    public HistoryFetchTask(Context context, IHistoryCallback iHistoryCallback){
        this.mContext = context;
        this.iHistoryCallback = iHistoryCallback;
    }

    @Override
    protected DataReadResponse doInBackground(Void... voids) {
        DataReadResponse dataReadResponse = null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long endTime = cal.getTimeInMillis();

        cal.add(Calendar.WEEK_OF_YEAR, -2);
        long startTime = cal.getTimeInMillis();

        GoogleSignInAccount mGoogleSignInAccount = GoogleSignIn.getLastSignedInAccount(mContext);
        if(mGoogleSignInAccount == null){
            mGoogleSignInAccount = MainActivity.getClient();
        }

        Task<DataReadResponse> response = Fitness.getHistoryClient((Activity) mContext, mGoogleSignInAccount)
                .readData(new DataReadRequest.Builder()
                .read(DataType.TYPE_STEP_COUNT_DELTA)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build());

        try {
            dataReadResponse = Tasks.await(response, 30, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataReadResponse;
    }

    @Override
    protected void onPostExecute(DataReadResponse dataReadResponse) {
        super.onPostExecute(dataReadResponse);

        iHistoryCallback.onComplete(dataReadResponse);
    }
}
