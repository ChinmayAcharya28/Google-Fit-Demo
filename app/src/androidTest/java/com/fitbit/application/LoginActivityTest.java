package com.fitbit.application;

import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.fitbit.application.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    Context mMockContext;
    ActivityScenario<LoginActivity> scenario;

    @Before
    public void init() {
        scenario = ActivityScenario.launch(LoginActivity.class);
        scenario.moveToState(Lifecycle.State.RESUMED);
        mMockContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void test_googleSignIn_visible() {

        onView(withId(R.id.sign_in_button)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

    }

    @Test
    public void test_setGoogleSignIn() {
        scenario.onActivity(new ActivityScenario.ActivityAction<LoginActivity>() {
            @Override
            public void perform(LoginActivity activity) {
                activity.initViews();
                activity.findViewById(R.id.sign_in_button).performClick();
            }
        });
    }

    @Test
    public void test_getGoogleApiClass() {

        scenario.onActivity(new ActivityScenario.ActivityAction<LoginActivity>() {
            @Override
            public void perform(LoginActivity activity) {
                FitnessOptions mFitnessOptions = FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                        .build();

                GoogleSignInAccount signInAccount = GoogleSignIn.getAccountForExtension(mMockContext, mFitnessOptions);
                GoogleSignInAccount googleSignInAccount = activity.getApiClinet(signInAccount);

                Assert.assertNotNull(googleSignInAccount);
            }
        });
    }

        @Test
        public void test_openMainActivity() {
            scenario.onActivity(new ActivityScenario.ActivityAction<LoginActivity>() {
                @Override
                public void perform(LoginActivity activity) {
                    activity.openNextActivity();
                }
            });
        }
    }


