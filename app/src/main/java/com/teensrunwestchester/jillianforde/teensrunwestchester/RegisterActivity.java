package com.teensrunwestchester.jillianforde.teensrunwestchester;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.teensrunwestchester.jillianforde.teensrunwestchester.data.BackendUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity {
    Typeface SANS_SERIF;
    // UI references.
    private AutoCompleteTextView mFirstName;
    private AutoCompleteTextView mLastName;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private String mString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize facebook login
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_register);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.email_login_form);
        mProgressView = (ProgressBar)findViewById(R.id.login_progress);
        //If the user is already logged in the same session, then send person to UpcommingPracticeActivity
        if(BackendUtil.isUserLoggedIn()){
            goToUpcomingPractices();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String email = mEmailView.getText().toString();
        outState.putString("email", email);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //TODO: HOW TO PUT THIS IN BACKENDUTIL?
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    public void onFacebookLogin(View view) {
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, Arrays.asList("email", "public_profile"), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    //No exceptions
                    if (user != null ) {
                        //New user
                        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.d("RegisterActivity", object.toString());
                                try {
                                    String firstName = object.getString("first_name");
                                    String lastName = object.getString("last_name");
                                    String email = object.getString("email");

                                    BackendUtil.saveUserInformation(firstName, lastName, email);
                                    goToUpcomingPractices();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                        //Request FB to return first_name, last_name and email
                        parameters.putString("fields", "first_name, last_name, email");
                        request.setParameters(parameters);
                        request.executeAsync();

                        //goToUpcomingPractices();
                    } else {
                        //The user cancelled the login
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void goToLoginActivity(){
        Intent goToLoginIntent = new Intent(this, LoginActivity.class);
        startActivity(goToLoginIntent);
    }

    public void onRegisterClick(View view) {
        register();
    }
    /**
     * Attempts to sign in or onRegisterClick the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void register() {// Reset errors.
        // Store values at the time of the login attempt.

        EditText fname = (EditText) findViewById(R.id.firstName);
        EditText lname = (EditText) findViewById(R.id.lastName);
        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);
        EditText confirmpass = (EditText) findViewById(R.id.confirmpassword);

        String fnameStr = fname.getText().toString();
        String lnameStr = lname.getText().toString();
        String emailStr = email.getText().toString();
        String passStr = password.getText().toString();
        String confirmpassStr = confirmpass.getText().toString();

        //check if user entered the same password in both fields.
        // if user didn't, then tell user that the passwords don't match
       /* if (!passStr.equals(confirmpassStr)) {
            passStr.setEror( "Error. Your passwords must match");
        }*/


        if (passStr.equals(confirmpassStr)) {
            boolean cancel = false;
            View focusView = null;

            // Check for a valid password, if the user entered one.
            //TODO: USE PASSWORD VALIDATION HERE
            if (!TextUtils.isEmpty(passStr) && !isPasswordValid(passStr)) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            } else if (TextUtils.isEmpty(passStr)) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }

            if (!passStr.equals(confirmpassStr)) {
                mPasswordView.setError(getString(R.string.passwords_must_match));
                focusView = mPasswordView;
                cancel = true;
            }

            // Check for a valid email address.
            if (TextUtils.isEmpty(emailStr)) {
                email.setError(getString(R.string.error_field_required));
                focusView = mEmailView;
                cancel = true;
            } else if (!isEmailValid(emailStr)) {
                mEmailView.setError(getString(R.string.error_invalid_email));
                focusView = mEmailView;
                cancel = true;
            }

            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                //If we get to this point is because all the info is right
                //should start the RegisterTask
                //new RegisterTask().execute(fnameStr, lnameStr, emailStr, passStr);

                //TODO: Check if signUp() works properly
                ParseUser newUser = new ParseUser();
                newUser.setUsername(emailStr);
                //newUser.setEmail(email);
                newUser.setPassword(passStr);
                newUser.put(Constants.PARSE_USER_FIRSTNAME, fnameStr);
                newUser.put(Constants.PARSE_USER_LASTNAME, lnameStr);

                newUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("Register", "Success");
                            Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            goToUpcomingPractices();
                        } else {
                            e.printStackTrace();
                            switch (e.getCode()) {
                                case ParseException.USERNAME_TAKEN:
                                    Toast.makeText(RegisterActivity.this, "This username is taken", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }
                });
            }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        boolean success = EmailFormatValidator.validate(email);
        if (!success) {
            this.mEmailView.setError(getString(R.string.error_incorrect_password));
        }
        return success;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public void goToUpcomingPractices() {
        Intent upcomingIntent = new Intent(this, UpcomingPracticeActivity.class);
        startActivity(upcomingIntent);
        finish();
    }

    public void goToLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);

        }
    }


    private class RegisterTask extends AsyncTask<String, Void, Integer>{

        @Override
        protected Integer doInBackground(String... params) {
            String firstName = params[0];
            String lastName = params[1];
            String email = params[2];
            String password = params[3];



            return BackendUtil.registerUser(firstName, lastName, email, password);
        }

        @Override
        protected void onPreExecute(){
            //The function that the template provided
            showProgress(true);
        }

        @Override
        protected void onPostExecute(Integer result){
            showProgress(false);
            LayoutInflater inflater = getLayoutInflater();
            View failedLoginLayout = inflater.inflate(R.layout.activity_register, null, false);
            Toast failedToLoginToast = new Toast(getApplicationContext());
            switch(result){
                case BackendUtil.REGISTER_SUCCESS:
                    Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    goToUpcomingPractices();
                case BackendUtil.USER_EXISTS:
                    Toast.makeText(RegisterActivity.this, "This username is taken", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}

