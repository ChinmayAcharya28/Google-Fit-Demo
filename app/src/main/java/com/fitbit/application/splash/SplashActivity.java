package com.fitbit.application.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.fitbit.application.MainActivity;
import com.fitbit.application.R;
import com.fitbit.application.login.LoginActivity;
import com.fitbit.application.utils.SharedPreference;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(SharedPreference.getFirstTimeLoggedIn(SplashActivity.this)){
                    openNextActivity(true, MainActivity.class);
                }else{
                    openNextActivity(true, LoginActivity.class);
                }

            }
        }, 5000);
    }

    private void openNextActivity(boolean b, Class activity) {
        Intent intent = new Intent(SplashActivity.this, activity);
        startActivity(intent);
        finish();
    }
}
