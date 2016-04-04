package com.teensrunwestchester.jillianforde.teensrunwestchester;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.teensrunwestchester.jillianforde.teensrunwestchester.data.BackendUtil;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private ProgressBar mProgress;
    private EditText mEmailEt;
    private EditText mPasswordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mProgress = (ProgressBar)findViewById(R.id.login_progress);
        mEmailEt = (EditText)findViewById(R.id.email);
        mPasswordEt = (EditText)findViewById(R.id.password);

    }

    public void launchUpcomingPracticeActivity() {
        Intent upcommingPracticeintent = new Intent(this, UpcomingPracticeActivity.class);
        startActivity(upcommingPracticeintent);
        finish();
    }

    //A function that is used by the onClick property in xml needs to meet 2 requirements:
    //1. Needs to be public
    //2. Needs to take a single parameter of type View
    public void goToResetPasswordActivity(View view){
        Intent goToResetPasswordActivity = new Intent(this, ResetPassword.class);
        startActivity(goToResetPasswordActivity);
    }

    public void goToRegisterActivity(View view){
        Intent goToRegisterActivity = new Intent(this, RegisterActivity.class);
        startActivity(goToRegisterActivity);
    }

    public void onClick(View view) {
        String username = mEmailEt.getText().toString();
        String password = mPasswordEt.getText().toString();

        new LoginTask().execute(username, password);

        /*ParseUser.requestPasswordResetInBackground(username, new RequestPasswordResetCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Context c = getApplicationContext();
                    CharSequence notifyResetPasswordEmail = "An email has been sent with instructions to reset your password.";
                    Toast notifyResetPasswordToast = Toast.makeText(c, notifyResetPasswordEmail, Toast.LENGTH_LONG);
                    notifyResetPasswordToast.show();
                }
            }
        });*/
    }

    private class LoginTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPostExecute(Integer result) {
            mProgress.setVisibility(View.GONE);
            LayoutInflater inflater = getLayoutInflater();
            switch (result) {
                case BackendUtil.LOGIN_SUCCESS:
                    //Success the user is now logged in
                    Toast.makeText(LoginActivity.this, "Logged in as " + BackendUtil.getUsername() + "!", Toast.LENGTH_SHORT).show();
                    launchUpcomingPracticeActivity();
                    break;
                case BackendUtil.LOGIN_WRONG_CREDENTIALS:
                    //The user typed in the wrong credentials
                    mPasswordEt.setError("The email or password you entered is incorrect.");
                    break;
                case BackendUtil.EMAIL_MISSING:
                    //user forgot to add their email address
                    mEmailEt.setError("Please enter your email address");
                    break;
                case BackendUtil.INVALID_EMAIL_ADDRESS:
                    //user didn't type in a valid email address, such as "ddd"
                    mEmailEt.setError("Please enter a valid email format");
                    break;
                case BackendUtil.USER_MISSING:
                    //user has not created an account
                    mEmailEt.setError("No account with this email exists. Please create an account.");
                    break;
                case BackendUtil.PASSWORD_MISSING:
                    //user has not created an account
                    mPasswordEt.setError("Please enter your password");
                    break;
                case BackendUtil.LOGIN_FAILURE:
                    //Generic failure
                    Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                    break;
            }

        }

        @Override
        protected void onPreExecute() {
            mProgress.setVisibility(View.VISIBLE);
        }

        //String... params refers to an array, it's just a different way to define it.
        @Override
        protected Integer doInBackground(String... params) {
            String username = params[0];
            String password = params[1];

            return BackendUtil.loginWithEmail(username, password);
        }
    }
}

