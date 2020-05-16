package com.fitbit.application;


import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.Lifecycle;
import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.fitbit.application.login.LoginActivity;
import com.fitbit.application.utils.SharedPreference;
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
public class MainActivityTest {

    Context mMockContext;
    ActivityScenario<MainActivity> scenario;

    @Before
    public void init() {
        scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.RESUMED);
        mMockContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void test_activityListVisible() {
        onView(withId(R.id.item_list)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void test_weekOrderClickVisible() {
        onView(withId(R.id.dateorder)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void test_setGoogleSignIn() {

        scenario.onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
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
    public void test_getGoogleApiClass() {

        scenario.onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
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
    public void test_openLoginActivity() {
        scenario.onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
                                @Override
                                public void perform(MainActivity activity) {
                                    SharedPreference.setFirstTimeLoggedIn(activity, true);
                                    Intent intent  = new Intent(activity, LoginActivity.class);
                                    activity.startActivity(intent);
                                }
        });
    }

    @Test
    public void test_history_api() {
        scenario.onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
               activity.initView();
               activity.initViewModel();
               activity.getHistorySteps();
            }
        });
    }

    @Test
    public void test_daily_api() {
        scenario.onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                activity.initView();
                activity.initViewModel();
                activity.getDailySteps();
            }
        });
    }


    @Test
    public void test_perform_click_on_week_api() {
        scenario.onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                activity.initView();
                activity.initViewModel();
                activity.getHistorySteps();
                activity.mLinearLayout.performClick();
            }
        });
    }

    @Test
    public void test_perform_check_list_is_reverse() {
        scenario.onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                activity.initView();
                activity.initViewModel();
                activity.getHistorySteps();
                Assert.assertEquals(true, activity.isReverse);
            }
        });
    }


    @Test
    public void test_perform_check_on_ascending() {
        scenario.onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                activity.initView();
                activity.initViewModel();
                activity.getHistorySteps();
                activity.mLinearLayout.performClick();
                Assert.assertEquals(false, activity.isReverse);
            }
        });
    }
}
