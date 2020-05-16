package com.fitbit.application.history.model;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fitbit.application.history.repository.HistoryFetchTask;
import com.fitbit.application.history.repository.IHistoryCallback;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.result.DataReadResponse;

import java.util.ArrayList;
import java.util.List;

public class HistoryViewModel extends ViewModel {

    public MutableLiveData<List<DataPoint>> mLiveData = new MutableLiveData<>();

    public MutableLiveData<List<DataPoint>> getLiveData(Context context) {
        new HistoryFetchTask(context, new IHistoryCallback() {
            @Override
            public void onComplete(DataReadResponse dataReadResponse) {
                setDataPoint(dataReadResponse);
            }
        }).execute();

        return mLiveData;
    }

    private void setDataPoint(DataReadResponse dataReadResponse) {

        if(dataReadResponse != null) {
            DataSet dataSet = dataReadResponse.getDataSet(DataType.TYPE_STEP_COUNT_DELTA);
            if(dataSet != null) {
                List<DataPoint> dataPoints = new ArrayList<>(dataSet.getDataPoints());
                mLiveData.setValue(dataPoints);
            }
        }

    }

}
