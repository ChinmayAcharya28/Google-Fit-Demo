package com.fitbit.application.history.repository;

import com.google.android.gms.fitness.result.DataReadResponse;

import java.util.ArrayList;

public interface IHistoryCallback {
    void onComplete(ArrayList<DataReadResponse> dataReadResponse);
}
