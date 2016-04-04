package com.teensrunwestchester.jillianforde.teensrunwestchester.takeattendance.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


import com.teensrunwestchester.jillianforde.teensrunwestchester.R;
import com.teensrunwestchester.jillianforde.teensrunwestchester.data.BackendUtil;
import com.teensrunwestchester.jillianforde.teensrunwestchester.takeattendance.AttendanceHistory;

import java.util.List;

/**
 * Created by Giorgio_Natili on 3/14/16.
 */
public class SubmitAttendanceTask extends AsyncTask <List <AttendanceHistory>, Void, List<String> > {

    private ProgressDialog mProgressDialog;
    private Context mContext;

    public SubmitAttendanceTask(Context context) {

        mContext = context;

    }

    @Override
    protected List<String> doInBackground(List<AttendanceHistory>... params) {

        List<String> names = BackendUtil.submitAttendance(params[0]);
        return names;

    }

    @Override
    protected void onPostExecute(List<String> names) {

        if (mProgressDialog != null) {

            mProgressDialog.dismiss();
        }

        Toast.makeText(mContext, mContext.getString(R.string.list_succesfully_submitted), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPreExecute() {

        mProgressDialog = ProgressDialog.show(mContext, "", mContext.getString(R.string.submitting_attendance));
        super.onPreExecute();

    }
}