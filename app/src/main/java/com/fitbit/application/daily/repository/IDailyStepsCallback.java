package com.fitbit.application.daily.repository;

import com.google.android.gms.fitness.result.DailyTotalResult;

public interface IDailyStepsCallback {
    void onComplete(DailyTotalResult dataReadResult);
}
