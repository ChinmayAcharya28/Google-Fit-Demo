package com.fitbit.application.history;

import com.google.android.gms.fitness.result.DataReadResult;

public interface IHistoryCallback {
    void onComplete(DataReadResult dataReadResult);
}
