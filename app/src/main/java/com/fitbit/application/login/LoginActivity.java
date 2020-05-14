package com.fitbit.application.login;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.fitbit.application.MainActivity;
import com.fitbit.application.R;
import com.fitbit.application.utils.SharedPreference;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.request.OnDataPointListener;

public class LoginActivity extends AppCompatActivity implements OnDataPointListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";

    private boolean mAuthInProgress;
    private GoogleApiClient mApiClient;
    private Context mContext;

    private SignInButton mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        if (savedInstanceState != null) {
            mAuthInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }

        initViews();

        if(SharedPreference.getFirstTimeLoggedIn(mContext)){
            openNextActivity();
        }
    }

    private void initViews() {
        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApiClinet().connect();

            }
        });
    }

    private GoogleApiClient getApiClinet(){
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.SENSORS_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        return mApiClient;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mApiClient != null) {
            mApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mApiClient != null) {
            Fitness.SensorsApi.remove(mApiClient, this)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            if (status.isSuccess()) {
                                mApiClient.disconnect();
                            }
                        }
                    });
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        SharedPreference.setFirstTimeLoggedIn(mContext, true);
        openNextActivity();
    }

    private void openNextActivity() {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if( !mAuthInProgress) {
            try {
                mAuthInProgress = true;
                connectionResult.startResolutionForResult( LoginActivity.this, REQUEST_OAUTH );
            } catch(IntentSender.SendIntentException e ) {
                Log.e( "StayFit", "sendingIntentException " + e.getMessage() );
            }
        } else {
            Log.e( "StayFit", "Authentication In Progress" );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == REQUEST_OAUTH ) {
            mAuthInProgress = false;
            if( resultCode == RESULT_OK ) {
                if( !mApiClient.isConnecting() && !mApiClient.isConnected() ) {
                    mApiClient.connect();
                }
            } else if( resultCode == RESULT_CANCELED ) {
                Log.e( "StayFit", "RESULT_CANCELED" );
            }
        } else {
            Log.e("StayFit", "RequestCode NOT request_oauth");
        }
    }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onDataPoint(DataPoint dataPoint) {

    }
}
