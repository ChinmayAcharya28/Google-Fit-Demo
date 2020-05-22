package com.fitbit.application.daily.model;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fitbit.application.daily.repository.CurrentStepFetchTask;
import com.fitbit.application.daily.repository.IDailyStepsCallback;
import com.fitbit.application.utils.Utils;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.result.DataReadResponse;

import java.util.List;

public class DailyViewModel extends ViewModel {

    public MutableLiveData<DataReadResponse> mLiveData = new MutableLiveData<>();

    public LiveData<DataReadResponse> getLiveData(Context mContext) {
        new CurrentStepFetchTask(mContext, new IDailyStepsCallback() {
            @Override
            public void onComplete(DataReadResponse dataReadResponse) {
                mLiveData.setValue(dataReadResponse);
            }
        }).execute();

        return mLiveData;
    }

    public String getSteps(List<DataPoint> dataPoints) {
        int count = 0;
        if(dataPoints != null && dataPoints.size() > 0) {
            for (DataPoint dataPoint: dataPoints) {
                if(Utils.isToday(dataPoint))
                count = count + dataPoint.getValue(dataPoint.getDataType().getFields().get(0)).asInt();
            }
        }
        return count+"";
    }
}
