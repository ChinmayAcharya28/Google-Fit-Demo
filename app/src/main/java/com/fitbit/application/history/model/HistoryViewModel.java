package com.fitbit.application.history.model;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fitbit.application.history.repository.HistoryFetchTask;
import com.fitbit.application.history.repository.IHistoryCallback;
import com.fitbit.application.utils.Utils;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.result.DataReadResponse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class HistoryViewModel extends ViewModel {

    public MutableLiveData<List<StepsModel>> mLiveData = new MutableLiveData<>();
    HashMap<String, Integer> map = new HashMap<>();

    public MutableLiveData<List<StepsModel>> getLiveData(Context context) {
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
            List<StepsModel> stepsModels = new ArrayList<>();
            map = new HashMap<>();
            for (int i = 0; i < dataReadResponse.getBuckets().size(); i++) {

                List<DataSet> dataSets = dataReadResponse.getBuckets().get(i).getDataSets();

                for (int j = 0; j < dataSets.size(); j++) {
                    DataSet dataSet = dataSets.get(j);
                    List<DataPoint> dataPoints =  dataSet.getDataPoints();

                    for (int k = 0; k < dataPoints.size(); k++) {

                        String date = Utils.convertStartDate(dataPoints.get(k));
                        DataPoint dataPoint = dataPoints.get(k);
                        List<Field> fields = dataPoints.get(k).getDataType().getFields();

                        for (int l = 0; l < fields.size(); l++) {
                            Field field = fields.get(l);
                            int value = 0;
                            if(field.getName().equalsIgnoreCase("steps")) {
                                value = dataPoint.getValue(fields.get(l)).asInt();
                                if(!map.containsKey(date)) {
                                    map.put(date, value);
                                }
                            }
                        }
                    }

                }
            }

            if(map != null && map.size() > 0)
                for (String date : map.keySet()) {
                    int value = map.get(date);
                    StepsModel stepsModel = new StepsModel(date, value);
                    stepsModels.add(stepsModel);
                }
            sortDate(stepsModels);
            mLiveData.setValue(stepsModels);

        }
    }

    public void sortDate(List<StepsModel> stepsModels){
        Collections.sort(stepsModels, new Comparator<StepsModel>() {
            DateFormat f = new SimpleDateFormat(Utils.DATE_FORMAT);
            @Override
            public int compare(StepsModel o1, StepsModel o2) {
                try {
                    return f.parse(o1.getDate()).compareTo(f.parse(o2.getDate()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
    }

}
