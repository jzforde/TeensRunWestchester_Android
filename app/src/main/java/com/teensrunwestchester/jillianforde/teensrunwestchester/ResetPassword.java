package com.teensrunwestchester.jillianforde.teensrunwestchester;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.teensrunwestchester.jillianforde.teensrunwestchester.data.BackendUtil;

/**
 * Created by jillianforde on 11/24/15.
 */
public class ResetPassword extends AppCompatActivity {
    private EditText mEmailEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        mEmailEt = (EditText) findViewById(R.id.email);
    }

    public void onClick(View view) {
        String email = mEmailEt.getText().toString();
        new ResetPasswordTask().execute(email);
    }

    private class ResetPasswordTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPostExecute(Integer result) {
            LayoutInflater inflater = getLayoutInflater();
            View failedToResetPasswordLayout = inflater.inflate(R.layout.reset_password, null, false);
            Toast failedToResetPasswordToast = new Toast(getApplicationContext());
            switch (result) {
                case BackendUtil.RESET_PASSWORD_SUCCESS:
                    //Success the user is now logged in
                    Toast.makeText(ResetPassword.this, "Email sent. You should receive an email with instructions shortly.", Toast.LENGTH_SHORT).show();
                    //BackendUtil.resetPassword(email);
                    break;
                case BackendUtil.EMAIL_NOT_FOUND:
                    //The user typed in the wrong credentials
                    Toast.makeText(ResetPassword.this, "Your email is not found ", Toast.LENGTH_SHORT).show();

                    break;
                /*case BackendUtil.EMAIL_MISSING:
                    //user forgot to add their email address
                    failedToResetPasswordToast.makeText(ResetPassword.this, "Please fill in your email address", Toast.LENGTH_SHORT).setView(failedToResetPasswordLayout);
                    failedToResetPasswordToast.show();
                    break;
                case BackendUtil.INVALID_EMAIL_ADDRESS:
                    //user didn't type in a valid email address, such as "ddd"
                    failedToResetPasswordToast.makeText(ResetPassword.this, "This email appears to be invalid", Toast.LENGTH_SHORT).setView(failedToResetPasswordLayout);
                    failedToResetPasswordToast.show();
                    break;
                case BackendUtil.USER_MISSING:
                    //user has not created an account
                    failedToResetPasswordToast.makeText(ResetPassword.this, "No account with this email address exists. Please create an account.", Toast.LENGTH_SHORT).setView(failedToResetPasswordLayout);
                    failedToResetPasswordToast.show();
                    break;*/
                case BackendUtil.RESET_PASSWORD_FAILURE:
                    //Generic failure
                    Toast.makeText(ResetPassword.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    //failedToResetPasswordToast.show();
                    break;
            }

        }

        //String... params refers to an array, it's just a different way to define it.
        @Override
        protected Integer doInBackground(String... params) {
            String email = params[0];
            return BackendUtil.resetPassword(email);
        }
    }
}

