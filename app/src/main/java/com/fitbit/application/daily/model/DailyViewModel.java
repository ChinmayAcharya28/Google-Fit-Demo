package com.fitbit.application.daily.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fitbit.application.daily.repository.CurrentStepFetchTask;
import com.fitbit.application.daily.repository.IDailyStepsCallback;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.result.DailyTotalResult;

public class DailyViewModel extends ViewModel {

    public MutableLiveData<DailyTotalResult> mLiveData = new MutableLiveData<>();

    public MutableLiveData<DailyTotalResult> getLiveData(GoogleApiClient apiClient) {
        new CurrentStepFetchTask(apiClient, new IDailyStepsCallback() {
            @Override
            public void onComplete(DailyTotalResult dataReadResult) {
                mLiveData.setValue(dataReadResult);
            }
        }).execute();

        return mLiveData;
    }

}
