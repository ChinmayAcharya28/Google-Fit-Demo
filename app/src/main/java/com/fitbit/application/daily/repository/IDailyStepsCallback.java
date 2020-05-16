package com.fitbit.application.daily.repository;

import com.google.android.gms.fitness.result.DataReadResponse;

public interface IDailyStepsCallback {
    void onComplete(DataReadResponse dataReadResponse);
}
