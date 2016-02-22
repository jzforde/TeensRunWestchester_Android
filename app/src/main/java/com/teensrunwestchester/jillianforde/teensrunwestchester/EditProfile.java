package com.teensrunwestchester.jillianforde.teensrunwestchester;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.teensrunwestchester.jillianforde.teensrunwestchester.data.BackendUtil;
import com.teensrunwestchester.jillianforde.teensrunwestchester.data.User;

public class EditProfile extends AppCompatActivity {
    //Don't need these unless I'm making changes to them.
    private TextView mFirstName;
    private TextView mLastName;
    private TextView mEmail;
    private TextView mPassword;
    private TextView mConfirmPassword;

    private EditText mFirstNameEt;
    private EditText mLastNameEt;
    private EditText mEmailEt;
    private EditText mPasswordEt;
    private EditText mConfirmPasswordEt;

    private Button mButton;

    private User mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCurrentUser = BackendUtil.getCurrentUser();

        mFirstName = (TextView) findViewById(R.id.profile_firstName);
        mLastName = (TextView) findViewById(R.id.profile_lastName);
        mEmail = (TextView) findViewById(R.id.profile_email);
        mPassword = (TextView) findViewById(R.id.profile_password);
        mConfirmPassword = (TextView) findViewById(R.id.profile_confirmPassword);

        mFirstNameEt = (EditText) findViewById(R.id.edit_firstName);
        mFirstNameEt.setText(mCurrentUser.getFirstName());

        mLastNameEt = (EditText) findViewById(R.id.edit_lastName);
        mLastNameEt.setText(mCurrentUser.getLastName());

        mEmailEt = (EditText) findViewById(R.id.edit_email);
        mEmailEt.setText(mCurrentUser.getEmail());

        mPasswordEt = (EditText) findViewById(R.id.edit_password);
        mConfirmPasswordEt = (EditText) findViewById(R.id.edit_confirmPassword);

        mButton = (Button) findViewById(R.id.save_changes_bt);

    }

    private boolean isEmailValid(String email) {
        boolean success = EmailFormatValidator.validate(email);
        return success;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    public void onSaveChangesClick(View view) {
        saveChanges();
    }

    public void saveChanges() {
        String fname = mFirstNameEt.getText().toString();
        String lname = mLastNameEt.getText().toString();
        String email = mEmailEt.getText().toString();
        String password = mPasswordEt.getText().toString();
        String confirmPassword = mConfirmPasswordEt.getText().toString();

        boolean cancel = false;
        View focusView = null;
        if (!password.isEmpty() && !isPasswordValid(password)) {
            mPasswordEt.setError(getString(R.string.password_too_short));
            focusView = mPasswordEt;
            cancel = true;
        }

        //Check if passwords match
        if (!password.equals(confirmPassword)) {
            mPasswordEt.setError(getString(R.string.passwords_must_match));
            focusView = mPasswordEt;
            cancel = true;
        }

        // Check for a valid email address.
        if (!isEmailValid(email)) {
            mEmailEt.setError(getString(R.string.error_invalid_email));
            focusView = mEmailEt;
            cancel = true;
        }

        if (!cancel) {
            new SaveChangesTask().execute(fname, lname, email, password);
        }
    }


    private class SaveChangesTask extends AsyncTask<String, Void, Integer> {
        private ProgressDialog progress;
        @Override
        protected Integer doInBackground(String... params) {
            String firstName = params[0];
            String lastName = params[1];
            String email = params[2];
            String password = params[3];

            return BackendUtil.updateUserProfile(firstName, lastName, email, password);
        }

        @Override
        protected void onPreExecute(){
            //The function that the template provided
            progress = ProgressDialog.show(EditProfile.this, "", getString(R.string.saving_changes), true, false);
        }

        @Override
        protected void onPostExecute(Integer result){
            if (progress != null)
                progress.dismiss();

            LayoutInflater inflater = getLayoutInflater();
            View failedLoginLayout = inflater.inflate(R.layout.activity_register, null, false);
            Toast failedToLoginToast = new Toast(getApplicationContext());
            switch(result){
                case BackendUtil.UPDATE_PROFILE_SUCCESS:
                    Toast.makeText(EditProfile.this, "Changes saved", Toast.LENGTH_SHORT).show();
                    break;
                case BackendUtil.EMAIL_TAKEN:
                    Toast.makeText(EditProfile.this, "Email is already taken", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(EditProfile.this, "Unable to save your changes", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
