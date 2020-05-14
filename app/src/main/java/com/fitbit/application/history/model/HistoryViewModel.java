package com.fitbit.application.history.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fitbit.application.history.repository.HistoryFetchTask;
import com.fitbit.application.history.repository.IHistoryCallback;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.result.DataReadResult;

import java.util.ArrayList;
import java.util.List;

public class HistoryViewModel extends ViewModel {

    public MutableLiveData<List<DataPoint>> mLiveData = new MutableLiveData<>();

    public MutableLiveData<List<DataPoint>> getLiveData(GoogleApiClient apiClient) {
        new HistoryFetchTask(apiClient, new IHistoryCallback() {
            @Override
            public void onComplete(DataReadResult dataReadResult) {
                setDataPoint(dataReadResult);
            }
        }).execute();

        return mLiveData;
    }

    private void setDataPoint(DataReadResult dataReadResult) {

        List<DataPoint> dataPoints = new ArrayList<>();

        if (dataReadResult.getBuckets().size() > 0) {
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    dataPoints.addAll(dataSet.getDataPoints());
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dataPoints.addAll(dataSet.getDataPoints());
            }
        }

        mLiveData.setValue(dataPoints);

    }

}
