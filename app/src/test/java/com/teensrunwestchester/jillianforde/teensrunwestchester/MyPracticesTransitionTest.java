package com.teensrunwestchester.jillianforde.teensrunwestchester;

import android.content.Intent;
import android.os.Build;
import android.widget.ListView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertThat;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by jillianforde on 1/21/16.
 * Tests that when a user clicks on a practice that user is taken to AttendanceActivity
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class MyPracticesTransitionTest {
    private MyProfileActivity activity;

    @Before
    public void setUp(){
        activity = Robolectric.setupActivity(MyProfileActivity.class);
    }

    @Test
    public void launchPracticeActivityOnClick(){
        ListView mMyPracticeLv = (ListView) activity.findViewById(R.id.my_practice_lv);
        //assertTrue()
        Intent practiceActivityIntent = new Intent(Intent.ACTION_VIEW);
        //Robolectric.buildActivity(AttendanceActivity.class).withIntent(practiceActivityIntent).create().get();
        AttendanceActivity activity = Robolectric.setupActivity(AttendanceActivity.class);
        activity.findViewById(R.id.)
        assertThat(shadowOf(activity).getNextStartedActivity()).isEqualTo(practiceActivityIntent);
        //1.) write a test that finds an activity. It will fail.

        //2.)write the code that creates the activity
        //3.)run test 1. it should pass
        //4.) test that menu item is clicked. It will fail
        //5.)write code to click on menu item
        //6.) test 4. should pass
        //7.) test taht intent goes to activity. it will fail
        //8.) create intent to go to activity
    }
}
