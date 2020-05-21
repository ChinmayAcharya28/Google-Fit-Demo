package com.fitbit.application.history.viewholder;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fitbit.application.R;
import com.fitbit.application.history.model.StepsModel;

public class HistoryViewHolder extends RecyclerView.ViewHolder {

    public TextView mStartTime;
    public TextView mEndTime;
    public TextView mSteps;

    public HistoryViewHolder(View itemView) {
        super(itemView);
        mStartTime = (TextView) itemView.findViewById(R.id.startTime);
        mEndTime = (TextView) itemView.findViewById(R.id.endTime);
        mEndTime.setVisibility(View.GONE);
        mSteps = (TextView) itemView.findViewById(R.id.steps);
    }

    @SuppressLint("SetTextI18n")
    public void populateModel(StepsModel stepsModel){
        mStartTime.setText("Date : "+ stepsModel.getDate());
       // mEndTime.setText("End : "+Utils.convertEndDate(dataPoint));
        mSteps.setText("Steps : "+ stepsModel.getValue());
    }
}
