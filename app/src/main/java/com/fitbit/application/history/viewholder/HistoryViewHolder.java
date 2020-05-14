package com.fitbit.application.history.viewholder;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fitbit.application.R;
import com.fitbit.application.utils.Utils;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.Field;

import java.util.List;

public class HistoryViewHolder extends RecyclerView.ViewHolder {

    public TextView mStartTime;
    public TextView mEndTime;
    public TextView mSteps;

    public HistoryViewHolder(View itemView) {
        super(itemView);
        mStartTime = (TextView) itemView.findViewById(R.id.startTime);
        mEndTime = (TextView) itemView.findViewById(R.id.endTime);
        mSteps = (TextView) itemView.findViewById(R.id.steps);
    }

    @SuppressLint("SetTextI18n")
    public void populateModel(DataPoint dataPoint, List<Field> fields){
        mStartTime.setText("Start : "+ Utils.convertDate(dataPoint));
        mEndTime.setText("End : "+Utils.convertDate(dataPoint));
        mSteps.setText("Steps : "+dataPoint.getValue(fields.get(0)));
    }
}
