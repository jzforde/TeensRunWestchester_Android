package com.teensrunwestchester.jillianforde.teensrunwestchester.takeattendance;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.teensrunwestchester.jillianforde.teensrunwestchester.R;
import com.teensrunwestchester.jillianforde.teensrunwestchester.data.BackendUtil;
import com.teensrunwestchester.jillianforde.teensrunwestchester.takeattendance.tasks.SubmitAttendanceTask;

import java.util.List;

//shows a listview of the attending users and allows mentors to take attendance
public class AttendanceMentorActivity extends AppCompatActivity implements
        MentorAttendance {

    public static final String EXTRA_PRACTICE_ID = "extra-practice-id";

    private ListView mAttendanceLv;
    private String mPracticeId;
    private List<AttendanceHistory> mAttendanceHistoryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        mAttendanceLv = (ListView)findViewById(R.id.attendance_listview);

        mPracticeId = getIntent().getStringExtra(EXTRA_PRACTICE_ID);

        Log.d("AttendanceMentorActivity", mPracticeId);
        new RetrieveAttendanceForPractice().execute(mPracticeId);

    }

    private class RetrieveAttendanceForPractice extends AsyncTask<String, Void, List<AttendanceHistory>> {
        @Override
        protected List<AttendanceHistory> doInBackground(String... params) {
            return BackendUtil.getAttendanceHistoryForPractice(params[0]);
        }

        @Override
        protected void onPostExecute(List<AttendanceHistory> attendanceHistories) {
            if (attendanceHistories != null) {
                mAttendanceHistoryList = attendanceHistories;
                AttendanceAdapter adapter = new AttendanceAdapter(AttendanceMentorActivity.this, attendanceHistories);
                mAttendanceLv.setAdapter(adapter);
            }
        }
    }


    public void submitAttendance(View view){

        new SubmitAttendanceTask(view.getContext()).execute(mAttendanceHistoryList);

    }

    private class AttendanceAdapter extends BaseAdapter{
        private Context mContext;
        private List<AttendanceHistory> mUserList;
        //implemented methods from BaseAdapter (getCount, getItem(id), getView
        public AttendanceAdapter(Context context, List<AttendanceHistory> userList) {
            mContext = context;
            mUserList = userList;
        }

        @Override
        public int getCount() {
            return mUserList.size();
        }

        @Override
        public AttendanceHistory getItem(int position) {
            return mUserList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Validate that convertView is new
            if (convertView == null) {
                //We don't want to attach convertView to parent yet, we pass in false as the final arg
                convertView = LayoutInflater.from(mContext).inflate(R.layout.attending_users_item, parent, false);
            }

            final AttendanceHistory item = getItem(position);
            TextView nameTv = (TextView)convertView.findViewById(R.id.runner_name);
            nameTv.setText(item.getUser().getFirstName());

            CheckBox attendanceBox = (CheckBox)convertView.findViewById(R.id.attendance_box);
            attendanceBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.setUserAttended(isChecked);
                }
            });
            attendanceBox.setChecked(item.getUserAttended());
            return convertView;
        }

    }
}
