package com.fitbit.application;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.fitbit.application.login.LoginActivity;
import com.fitbit.application.utils.SharedPreference;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Context mContext;
    boolean isReverse;

    ImageView mWeekAscOrDesc;
    TextView mDailyStepsValueTextView;
    LinearLayout mLinearLayout;

    RecyclerView mRecyclerView;
    HistoryAdapter mHistoryAdapter;

    HistoryViewModel mHistoryViewModel;
    DailyViewModel mDailyViewModel;
    static GoogleSignInAccount mGoogleSignInAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();

        mHistoryViewModel =  ViewModelProviders.of((FragmentActivity) mContext).get(HistoryViewModel.class);
        mDailyViewModel =  ViewModelProviders.of((FragmentActivity) mContext).get(DailyViewModel.class);

        mHistoryViewModel.getLiveData(mContext).observe(this, new Observer<List<DataPoint>>() {
            @Override
            public void onChanged(List<DataPoint> dataPoints) {
                mHistoryAdapter = new HistoryAdapter(R.layout.history_row_view, (ArrayList<DataPoint>) dataPoints);
                mRecyclerView.setAdapter(mHistoryAdapter);
                mHistoryAdapter.notifyDataSetChanged();
            }
        });

        mDailyViewModel.getLiveData(mContext).observe(this, new Observer<DataReadResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChanged(DataReadResponse dataReadResponse) {
                if(dataReadResponse != null) {
                    DataSet dataSet = dataReadResponse.getDataSet(DataType.TYPE_STEP_COUNT_DELTA);
                    String count = mDailyViewModel.getSteps(dataSet.getDataPoints());
                    if (!TextUtils.isEmpty(count)) {
                        mDailyStepsValueTextView.setText("" + count);
                    } else {
                        mDailyStepsValueTextView.setText("0");
                    }
                }
            }
        });

    }

    private GoogleSignInAccount getApiClinet(GoogleSignInAccount mSignInClient){

        FitnessOptions mFitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        mSignInClient = GoogleSignIn.getAccountForExtension(this, mFitnessOptions);
        return mSignInClient;
    }

    private void initView(){
        isReverse = true;

        mWeekAscOrDesc = (ImageView) findViewById(R.id.updownimage);
        mLinearLayout = (LinearLayout) findViewById(R.id.dateorder);
        mDailyStepsValueTextView = (TextView) findViewById(R.id.todaystepsvalue);
        mRecyclerView = (RecyclerView) findViewById(R.id.item_list);
        mRecyclerView.setLayoutManager(getReverseManager());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isReverse) {
                    isReverse = false;
                    mWeekAscOrDesc.setImageResource(android.R.drawable.arrow_up_float);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                }else{
                    isReverse = true;
                    mWeekAscOrDesc.setImageResource(android.R.drawable.arrow_down_float);
                    mRecyclerView.setLayoutManager(getReverseManager());
                }
            }
        });
    }

    private LinearLayoutManager getReverseManager(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        return linearLayoutManager;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.sign_out);

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

               if(GoogleSignIn.getLastSignedInAccount(mContext) != null){
                   GoogleSignInOptions signInOptions = GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN;
                   GoogleSignIn.getClient(mContext, signInOptions).signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           SharedPreference.setFirstTimeLoggedIn(mContext, false);
                           Intent intent = new Intent(mContext, LoginActivity.class);
                           startActivity(intent);
                           finish();
                       }
                   });
               }
                return false;
            }
        });
        return true;
    }

    public static void setClient(GoogleSignInAccount apiClinet) {
       mGoogleSignInAccount = apiClinet;
    }

    public static GoogleSignInAccount getClient(){
        return mGoogleSignInAccount;
    }

}
