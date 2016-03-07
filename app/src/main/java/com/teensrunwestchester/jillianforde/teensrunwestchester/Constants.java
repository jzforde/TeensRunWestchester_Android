package com.teensrunwestchester.jillianforde.teensrunwestchester;

/**
 * Created by jillianforde on 11/12/15.
 */
public class Constants {

    //The APP id and Client Key for this app in Parse.com
    public static final String PARSE_APP_ID = "ASK_JILLIAN_IS_A_SECREET";
    public static final String PARSE_CLIENT_ID = "ASK_JILLIAN_IS_A_SECREET";

    //Table names
    public static final String PARSE_TABLE_USER = "_User";
    public static final String PARSE_TABLE_PRACTICE = "PracticeActivity";
    public static final String PARSE_TABLE_ATTENDANCEHISTORY = "AttendanceHistory";

    //Columns in User table
    public static final String PARSE_USER_FIRSTNAME = "firstName";
    public static final String PARSE_USER_LASTNAME = "lastName";
    public static final String PARSE_USER_ISMENTOR = "isMentor";

    //Columns in PracticeActivity table
    public static final String PARSE_PRACTICE_OBJECTID = "objectId";
    public static final String PARSE_PRACTICE_DUE = "dueDate";
    public static final String PARSE_PRACTICE_NAME = "name";
    public static final String PARSE_PRACTICE_ATTENDINGUSERS = "attendingUsers";
    public static final String PARSE_PRACTICE_LOCATIONNAME = "locationName";
    public static final String PARSE_PRACTICE_LOCATION = "location";
    public static final String PARSE_PRACTICE_MENTORPOINTER = "mentor";
    public static final String PARSE_PRACTICE_MILESRUN = "milesRun";
    public static final String PARSE_PRACTICE_TIME = "practiceTime";
    public static final String PARSE_PRACTICE_TESTDATE = "testNameDate";

    public static final String PARSE_ATTENDANCEHISTORY_PRACTICE = "practice";
    public static final String PARSE_ATTENDANCEHISTORY_USER = "user";
    public static final String PARSE_ATTENDANCEHISTORY_DIDATTEND = "didAttend";

    public static final String PARSE_USER_EMAIL = "email";
}
