package com.teensrunwestchester.jillianforde.teensrunwestchester;

import android.os.Build;
import android.view.View;

import com.teensrunwestchester.jillianforde.teensrunwestchester.takeattendance.AttendanceHistory;
import com.teensrunwestchester.jillianforde.teensrunwestchester.takeattendance.tasks.SubmitAttendanceTask;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLooper;
import org.robolectric.util.Transcript;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by jillianforde on 10/31/15.
 *
 * This class tests the Login functionality
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class TestLogin {

    private SubmitAttendanceTask mSubmitAttendanceTask;
    private Transcript mTranscript;

    @Before
    public void setUp() throws Exception {

        mTranscript = new Transcript();
        mSubmitAttendanceTask = new SubmitAttendanceTask(RuntimeEnvironment.application.getApplicationContext());

    }

    @Test
    public void testEmailAddresses() throws InterruptedException, ExecutionException, TimeoutException {

        List<AttendanceHistory> attendanceHistories = new ArrayList<AttendanceHistory>();

        mSubmitAttendanceTask.execute(attendanceHistories);
        mTranscript.assertEventsSoFar("onPreExecute");

        ShadowApplication.runBackgroundTasks();

        mTranscript.assertEventsSoFar("doInBackground a, b");
        assertEquals("Result should get stored in the AsyncTask", "c", mSubmitAttendanceTask.get(100, TimeUnit.MILLISECONDS));

        ShadowLooper.runUiThreadTasks();
        mTranscript.assertEventsSoFar("onPostExecute c");

        mSubmitAttendanceTask.execute();

    }

    @After
    public void tearDown() throws Exception {

    }
}

