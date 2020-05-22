package com.fitbit.application.history.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fitbit.application.history.model.StepsModel;
import com.fitbit.application.history.viewholder.HistoryViewHolder;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    private int mListItemLayout;
    private ArrayList<StepsModel> mDataSetList;

    public HistoryAdapter(int layoutId, ArrayList<StepsModel> itemList) {
        this.mListItemLayout = layoutId;
        this.mDataSetList = itemList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mListItemLayout, parent, false);
        return new HistoryViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        StepsModel dataPoint = mDataSetList.get(position);
        holder.populateModel(dataPoint);
    }

    @Override
    public int getItemCount() {
        return mDataSetList == null ? 0 : mDataSetList.size();
    }
}
