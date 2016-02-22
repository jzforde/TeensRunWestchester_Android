package com.teensrunwestchester.jillianforde.teensrunwestchester;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.teensrunwestchester.jillianforde.teensrunwestchester.data.BackendUtil;
import com.teensrunwestchester.jillianforde.teensrunwestchester.data.PracticeActivityEvent;

import java.util.List;
/*
Finds the upcomming practices and displays them to user
 */
public class UpcomingPracticeActivity extends AppCompatActivity {

    private ListView mPracticeLv;
    private ArrayAdapter mPracticeAdapter;
    private List<PracticeActivityEvent> mUpcomingEvents;
    private TextView mNoPracticesTv;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_practices);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPracticeLv = (ListView)findViewById(R.id.expandableListView);
        mNoPracticesTv = (TextView)findViewById(R.id.no_practices_message);

        new FindUpcomingPracticesTask().execute();
    }

    private void setupUpcomingPracticesList() {
        if(mUpcomingEvents == null || mUpcomingEvents.size() == 0) {
            mPracticeLv.setVisibility(View.GONE);
            mNoPracticesTv.setVisibility(View.VISIBLE);
            return;
        } else {
            mPracticeLv.setVisibility(View.VISIBLE);
            mNoPracticesTv.setVisibility(View.GONE);
        }

        mPracticeAdapter = new ArrayAdapter<PracticeActivityEvent>(this,
                R.layout.upcoming_practice_item,
                R.id.text, mUpcomingEvents);

        //Here check if practices list is null, then say "no upcoming practices", else
        mPracticeLv.setAdapter(mPracticeAdapter);
        mPracticeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //Open up the detailed view of the practice session when the user clicks the practice date.
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                PracticeActivityEvent practice = mUpcomingEvents.get(position);
                Intent launchDetailActivity = new Intent(UpcomingPracticeActivity.this, AttendanceActivity.class).
                        putExtra(AttendanceActivity.EXTRA_DETAILS, practice);
                startActivity(launchDetailActivity);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_check_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Basically item is whatever button was clicked in the action bar
        int id = item.getItemId();

        //return false will propagate the event accross the system, other views
        //will be notified about it. return true tells the system that you already
        //handled the event and other views dont need to know about it
        if (id == R.id.action_my_profile) {
            //In this scenario this keyword works because it refers to the Activity a.k.a the Context
            //we are launching the new activity from
            Intent profileIntent = new Intent(this, MyProfileActivity.class);
            startActivity(profileIntent);
            return true;
        }
        //log out of the app
        if(id == R.id.action_log_out){
            Intent logoutIntent = new Intent(this, RegisterActivity.class);
            startActivity(logoutIntent);
            finish();
            BackendUtil.logOut();
            return true;
        }

        if (id == R.id.action_edit_profile) {
            Intent editProfileIntent = new Intent(this, EditProfile.class);
            startActivity(editProfileIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class FindUpcomingPracticesTask extends AsyncTask<Void, Void, PracticeListWrapper> {
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
                    Toast.makeText(UpcomingPracticeActivity.this, "Connection failure", Toast.LENGTH_SHORT).show();
                } else if (wrapper.resultCode == BackendUtil.OBJECT_NOT_FOUND) {
                    Toast.makeText(UpcomingPracticeActivity.this, "No objects found!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UpcomingPracticeActivity.this, "Something wrong happened!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            mUpcomingEvents = wrapper.theList;
            //We actually have objects!!
            setupUpcomingPracticesList();
        }

        @Override
        protected void onPreExecute() {
            mProgress = ProgressDialog.show(UpcomingPracticeActivity.this,
                    "", "Retrieving practices...",
                    true, false);
        }

        @Override
        protected PracticeListWrapper doInBackground(Void... params) {
            return BackendUtil.getUpcomingPracticeEvents();
        }
    }
}
