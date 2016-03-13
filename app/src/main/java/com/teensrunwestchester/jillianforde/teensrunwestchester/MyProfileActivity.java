package com.teensrunwestchester.jillianforde.teensrunwestchester;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.teensrunwestchester.jillianforde.teensrunwestchester.data.BackendUtil;
import com.teensrunwestchester.jillianforde.teensrunwestchester.data.PracticeActivityEvent;
import com.teensrunwestchester.jillianforde.teensrunwestchester.takeattendance.AttendanceActivity;

import java.util.List;

public class MyProfileActivity extends AppCompatActivity {

    private ListView mMyPracticeLv;
    private ArrayAdapter mAdapter;
    private List<PracticeActivityEvent> mPracticeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        mMyPracticeLv = (ListView)findViewById(R.id.my_practice_lv);
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        new FindUpcomingPracticesTask().execute();
    }

    private void setupListView(List<PracticeActivityEvent> practices) {
        mPracticeList = practices;
        mAdapter = new ArrayAdapter<PracticeActivityEvent>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, mPracticeList);
        mMyPracticeLv.setAdapter(mAdapter);
        mMyPracticeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //Open up the detailed view of the practice session when the user clicks the practice date.
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                PracticeActivityEvent practice = mPracticeList.get(position);
                Intent launchAttendanceActivity = new Intent(MyProfileActivity.this, AttendanceActivity.class).
                        putExtra(AttendanceActivity.EXTRA_DETAILS, practice);
                startActivity(launchAttendanceActivity);
            }
        });
    }



    private class FindUpcomingPracticesTask extends AsyncTask<Void, Void, PracticeListWrapper> {
        private ProgressDialog mProgress;

        @Override
        protected void onPostExecute(PracticeListWrapper wrapper) {
            if (mProgress != null) {
                mProgress.dismiss();
                mProgress = null;
            }

            if (wrapper.theList == null) {
                //Notify the user that something wrong happened, other than not having any objects
                //that match the query constraints
                if (wrapper.resultCode == BackendUtil.CONNECTION_FAILURE) {
                    Toast.makeText(MyProfileActivity.this, "Connection failure", Toast.LENGTH_SHORT).show();
                } else if (wrapper.resultCode == BackendUtil.OBJECT_NOT_FOUND) {
                    Toast.makeText(MyProfileActivity.this, "No objects found!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyProfileActivity.this, "Something wrong happened!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            //We actually have objects!!
            setupListView(wrapper.theList);
        }

        @Override
        protected void onPreExecute() {
            mProgress = ProgressDialog.show(MyProfileActivity.this,
                    "", "Retrieving practices...",
                    true, false);
            Log.d("MyProfileActivity","Reached onPreExecute");
        }

        @Override
        protected PracticeListWrapper doInBackground(Void... params) {
            return BackendUtil.getMyPractices();
        }


    }
}
