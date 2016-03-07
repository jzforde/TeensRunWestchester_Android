package com.teensrunwestchester.jillianforde.teensrunwestchester.takeattendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teensrunwestchester.jillianforde.teensrunwestchester.R;
import com.teensrunwestchester.jillianforde.teensrunwestchester.data.BackendUtil;
import com.teensrunwestchester.jillianforde.teensrunwestchester.data.PracticeActivityEvent;

/**
 * Created by jillianforde on 11/1/15.
 */
public class AttendanceActivity extends AppCompatActivity{

    public static final String EXTRA_DETAILS = "extra-details";

    private TextView mDetailsTextView;
    private Button mRegisterAttendanceBt;
    private Button mCancelAttendanceBt;
    private PracticeActivityEvent mPractice;
    private ImageView mDirectionsIcon;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private Button mTakeAttendanceBt;
    //latitude and longitude
    //this is tibbets brook park
    //private LatLng practiceLocation = new LatLng(40.927047, -73.876965);
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_activity);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);

        mTakeAttendanceBt = (Button)findViewById(R.id.take_attendance_button);

        if (BackendUtil.isMentorStatus()) {
            mTakeAttendanceBt.setVisibility(View.VISIBLE);
        }

        mDetailsTextView = (TextView) findViewById(R.id.details);
        mPractice = getIntent().getParcelableExtra(EXTRA_DETAILS);

        mRegisterAttendanceBt = (Button) findViewById(R.id.attendance_button);
        mCancelAttendanceBt = (Button) findViewById(R.id.cancel_attendance_button);
        mDirectionsIcon = (ImageView) findViewById(R.id.directions_icon);
        //Map of location of practice
        mMapView = (MapView) findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);

        MapsInitializer.initialize(this);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mPractice.getLocation(), 14);
                mGoogleMap.animateCamera(cameraUpdate);
                googleMap.addMarker(new MarkerOptions().position(mPractice.getLocation()).title(mPractice.getmLocationName()));
            }
        });


        mDetailsTextView.setText(mPractice.getName() + "\n" + mPractice.getmLocationName() + "\n" + mPractice.getmPracticeTime() + "\n" + "Mentor attending practice: " + mPractice.getmMentorName());
        Log.d("AttendanceActivity", mPractice.getName() + "\n" + mPractice.getmLocationName());

        new CheckAttendanceTask().execute(mPractice.getId());


    }

    //Get directions to practice from clicking icon
    public void getDirections(View view) {
        String geoFormat = "geo:%f,%f?q=%s";
        String geoString = String.format(geoFormat, mPractice.getLocation().latitude, mPractice.getLocation().longitude, mPractice.getmLocationName().replace(" ", "+"));
        Uri TibbettsUri = Uri.parse(geoString);
        Intent directionsIntent = new Intent(Intent.ACTION_VIEW, TibbettsUri);
        directionsIntent.setPackage("com.google.android.apps.maps");
        startActivity(directionsIntent);
    }

    //The GOogle Maps library requires that we forward the Activity lifecylce methods to
    //the Map View
    @Override
    protected void onPause() {
        super.onPause();

        if (mMapView != null)
            mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMapView != null)
            mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mMapView != null)
            mMapView.onResume();
    }

    public void attendPractice(View view) {
        new AttendPracticeTask().execute(mPractice.getId());
    }

    //String is the value type we pass for the arguments (in this is case is the id)
    //Void is the value type of the progress, in this task we dont care about it
    //Integer is the type of the result of the task (result codes in this case)
    public void cancelAttendance(View view) {
        new CancelAttendanceTask().execute(mPractice.getId());
    }

    public void takeAttendance(View v){
        Intent takeAttendanceIntent = new Intent(this, AttendanceMentorActivity.class);
        takeAttendanceIntent.putExtra(AttendanceMentorActivity.EXTRA_PRACTICE_ID, mPractice.getId());
        startActivity(takeAttendanceIntent);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    private class CancelAttendanceTask extends AsyncTask<String, Void, Integer> {
        private ProgressDialog mProgress;

        @Override
        protected Integer doInBackground(String... params) {
            return BackendUtil.cancelAttendance(params[0]);
        }

        @Override
        protected void onPreExecute() {
            mProgress = ProgressDialog.show(AttendanceActivity.this, "", "Canceling your attendance...",
                    true, false);
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (mProgress != null)
                mProgress.dismiss();

            switch (result) {
                case BackendUtil.OBJECT_NOT_FOUND:
                    Toast.makeText(AttendanceActivity.this, "Couldn't find the practice with that id!", Toast.LENGTH_SHORT).show();
                    break;

                case BackendUtil.SAVE_FAILURE:
                    Toast.makeText(AttendanceActivity.this, "Couldn't save your cancellation!", Toast.LENGTH_SHORT).show();
                    break;

                case BackendUtil.SAVE_SUCCESS:
                    Toast.makeText(AttendanceActivity.this, "Your attendance has been cancelled.", Toast.LENGTH_LONG).show();
                    mRegisterAttendanceBt.setVisibility(View.VISIBLE);
                    mCancelAttendanceBt.setVisibility(View.GONE);

                    break;
            }
        }
    }

    private class AttendPracticeTask extends AsyncTask<String, Void, Integer> {
        private ProgressDialog mProgress;

        @Override
        protected Integer doInBackground(String... params) {
            return BackendUtil.registerAttendance(params[0]);
        }

        @Override
        protected void onPreExecute() {
            mProgress = ProgressDialog.show(AttendanceActivity.this, "", "Registering your attendance...",
                    true, false);
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (mProgress != null)
                mProgress.dismiss();

            switch (result) {
                case BackendUtil.OBJECT_NOT_FOUND:
                    Toast.makeText(AttendanceActivity.this, "Couldn't find the practice with that id!", Toast.LENGTH_SHORT).show();
                    break;

                case BackendUtil.SAVE_FAILURE:
                    Toast.makeText(AttendanceActivity.this, "Couldn't save your attendance! Please check your internet connection.", Toast.LENGTH_SHORT).show();
                    break;

                case BackendUtil.SAVE_SUCCESS:
                    Toast.makeText(AttendanceActivity.this, "Your attendance is now confirmed", Toast.LENGTH_SHORT).show();
                    mRegisterAttendanceBt.setVisibility(View.GONE);
                    mCancelAttendanceBt.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private class CheckAttendanceTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String...params) {
            return BackendUtil.isUserAttending(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean isUserAttending) {
            if (!isUserAttending) {
                mRegisterAttendanceBt.setVisibility(View.VISIBLE);
            } else {
                mCancelAttendanceBt.setVisibility(View.VISIBLE);
            }
        }
    }

    //If user is mentor and time and date are practice start time, then enable "take attendance" button
    // which will open up takeAttendance activity.
}
