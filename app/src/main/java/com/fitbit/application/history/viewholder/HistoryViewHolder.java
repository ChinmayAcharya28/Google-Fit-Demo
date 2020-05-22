package com.fitbit.application.history.viewholder;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fitbit.application.R;
import com.fitbit.application.history.model.StepsModel;
import com.fitbit.application.utils.Utils;

public class HistoryViewHolder extends RecyclerView.ViewHolder {

    public TextView mStartTime;
    public TextView mSteps;

    public HistoryViewHolder(View itemView) {
        super(itemView);
        mStartTime = (TextView) itemView.findViewById(R.id.startTime);
        mSteps = (TextView) itemView.findViewById(R.id.steps);
    }

    @SuppressLint("SetTextI18n")
    public void populateModel(StepsModel stepsModel){
        mStartTime.setText(Utils.formatToYesterdayOrToday(stepsModel.getDate()));
        mSteps.setText("Steps : "+ stepsModel.getValue());
    }
}
