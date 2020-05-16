package com.fitbit.application.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.fitbit.application.MainActivity;
import com.fitbit.application.R;
import com.fitbit.application.utils.SharedPreference;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";

    private boolean mAuthInProgress;
    private Context mContext;

    private Button mSignInButton;

    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInOptionsExtension mFitnessOptions;

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

    public void initViews() {
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions signInOptions = GoogleSignInOptions.DEFAULT_SIGN_IN;
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(mContext, signInOptions);
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });
    }

    public GoogleSignInAccount getApiClinet(GoogleSignInAccount mSignInClient){

        mFitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        mSignInClient = GoogleSignIn.getAccountForExtension(this, mFitnessOptions);
        return mSignInClient;
    }

    public void openNextActivity() {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            boolean isException = false;
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                try {
                    GoogleSignInAccount googleSignIn = task.getResult(ApiException.class);
                } catch (ApiException e) {
                    isException = true;
                    ResolvableApiException apiException = ((ResolvableApiException)e);
                    try {
                        apiException.startResolutionForResult((Activity) mContext, RC_SIGN_IN);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }

                if(!isException) {
                    mAuthInProgress = true;
                    SharedPreference.setFirstTimeLoggedIn(mContext, true);
                    openNextActivity();
                }
            } else {
                Log.e( "StayFit", "Authentication failed" );
            }
        }
    }



}
