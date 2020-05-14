package com.fitbit.application.history.repository;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HistoryFetchTask extends AsyncTask<Void, DataReadResult, DataReadResult> {

    GoogleApiClient mApiClient;
    IHistoryCallback iHistoryCallback;

    public HistoryFetchTask(GoogleApiClient apiClient, IHistoryCallback iHistoryCallback){
        this.mApiClient = apiClient;
        this.iHistoryCallback = iHistoryCallback;
    }

    @Override
    protected DataReadResult doInBackground(Void... voids) {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();

        cal.add(Calendar.WEEK_OF_YEAR, -2);
        long startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = DateFormat.getDateInstance();
        Log.e("History", "Range Start: " + dateFormat.format(startTime));
        Log.e("History", "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        DataReadResult dataReadResult = Fitness.HistoryApi.readData(mApiClient, readRequest).await(1, TimeUnit.MINUTES);

        return dataReadResult;
    }

    @Override
    protected void onPostExecute(DataReadResult dataReadResult) {
        super.onPostExecute(dataReadResult);

        iHistoryCallback.onComplete(dataReadResult);
    }
}
