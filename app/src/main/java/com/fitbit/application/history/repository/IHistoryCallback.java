package com.fitbit.application.history.repository;

import com.google.android.gms.fitness.result.DataReadResponse;

public interface IHistoryCallback {
    void onComplete(DataReadResponse dataReadResponse);
}
