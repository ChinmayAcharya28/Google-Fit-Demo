package com.fitbit.application.daily.repository;

import android.os.AsyncTask;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.result.DailyTotalResult;

import java.util.concurrent.TimeUnit;

public class CurrentStepFetchTask extends AsyncTask<Void, DailyTotalResult, DailyTotalResult> {

    GoogleApiClient mApiClient;
    IDailyStepsCallback iDailyStepsCallback;

    public CurrentStepFetchTask(GoogleApiClient apiClient, IDailyStepsCallback iDailyStepsCallback){
        this.mApiClient = apiClient;
        this.iDailyStepsCallback = iDailyStepsCallback;
    }

    @Override
    protected DailyTotalResult doInBackground(Void... voids) {
        DailyTotalResult result = Fitness.HistoryApi.readDailyTotal( mApiClient, DataType.TYPE_STEP_COUNT_DELTA ).await(1, TimeUnit.MINUTES);
        return result;
    }

    @Override
    protected void onPostExecute(DailyTotalResult dailyTotalResult) {
        super.onPostExecute(dailyTotalResult);
        iDailyStepsCallback.onComplete(dailyTotalResult);
    }
}
