package com.teensrunwestchester.jillianforde.teensrunwestchester;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by jillianforde on 11/12/15.
 */
public class TeensRunWestchesterApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, Constants.PARSE_APP_ID, Constants.PARSE_CLIENT_ID);
        ParseFacebookUtils.initialize(this);
    }
}
