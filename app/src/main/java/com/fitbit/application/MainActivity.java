package com.fitbit.application;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fitbit.application.daily.model.DailyViewModel;
import com.fitbit.application.history.adapter.HistoryAdapter;
import com.fitbit.application.history.model.HistoryViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.result.DailyTotalResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    Context mContext;

    TextView mDailyStepsValueTextView;

    RecyclerView mRecyclerView;
    HistoryAdapter mHistoryAdapter;

    HistoryViewModel mHistoryViewModel;
    private GoogleApiClient mApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        mApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(Fitness.SENSORS_API)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .enableAutoManage(this, 0, this)
                .build();
        mApiClient.connect();

        mDailyStepsValueTextView = (TextView) findViewById(R.id.todaystepsvalue);
        mRecyclerView = (RecyclerView) findViewById(R.id.item_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mHistoryViewModel =  ViewModelProviders.of((FragmentActivity) mContext).get(HistoryViewModel.class);

        mHistoryViewModel.getLiveData(mApiClient).observe(this, new Observer<List<DataPoint>>() {
            @Override
            public void onChanged(List<DataPoint> dataPoints) {
                mHistoryAdapter = new HistoryAdapter(R.layout.history_row_view, (ArrayList<DataPoint>) dataPoints);
                mRecyclerView.setAdapter(mHistoryAdapter);
                mHistoryAdapter.notifyDataSetChanged();
            }
        });

        new DailyViewModel().getLiveData(mApiClient).observe(this, new Observer<DailyTotalResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChanged(DailyTotalResult dailyTotalResult) {
                DataPoint dataPoint = Objects.requireNonNull(dailyTotalResult.getTotal()).getDataPoints().get(0);
                mDailyStepsValueTextView.setText(""+dataPoint.getValue(dataPoint.getDataType().getFields().get(0)));
            }
        });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
