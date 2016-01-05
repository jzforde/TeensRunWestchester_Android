package com.teensrunwestchester.jillianforde.teensrunwestchester;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jillianforde on 11/3/15.
 * Helper class to validate the format of an email address
 */
public class EmailFormatValidator {

    private static Matcher matcher;

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9]+(\\.[_A-Za-z0-9]+)*@"
            + "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private  static Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public EmailFormatValidator(){
    }

    public static boolean validate(final String email){
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public Matcher getMatcher(){
        return matcher;
    }

}
