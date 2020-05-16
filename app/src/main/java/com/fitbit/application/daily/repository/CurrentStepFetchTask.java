package com.fitbit.application.daily.repository;

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

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CurrentStepFetchTask extends AsyncTask<Void, DataReadResponse, DataReadResponse> {

    Context mContext;
    IDailyStepsCallback iDailyStepsCallback;

    public CurrentStepFetchTask(Context context, IDailyStepsCallback iDailyStepsCallback) {
        this.mContext = context;
        this.iDailyStepsCallback = iDailyStepsCallback;
    }

    @Override
    protected DataReadResponse doInBackground(Void... voids) {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();

        cal.add(Calendar.HOUR_OF_DAY, -24);
        long startTime = cal.getTimeInMillis();

        GoogleSignInAccount mGoogleSignInAccount = GoogleSignIn.getLastSignedInAccount(mContext);
        if(mGoogleSignInAccount == null){
            mGoogleSignInAccount = MainActivity.getClient();
        }

        Task<DataReadResponse> response = Fitness.getHistoryClient(mContext, mGoogleSignInAccount)
                .readData(new DataReadRequest.Builder()
                        .read(DataType.TYPE_STEP_COUNT_DELTA)
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                        .build());

        DataReadResponse readDataResult = null;
        try {
            readDataResult = Tasks.await(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readDataResult;
    }

    @Override
    protected void onPostExecute(DataReadResponse dataReadResponse) {
        super.onPostExecute(dataReadResponse);
        iDailyStepsCallback.onComplete(dataReadResponse);
    }
}
