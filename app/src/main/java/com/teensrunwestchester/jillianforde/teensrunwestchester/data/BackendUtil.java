package com.teensrunwestchester.jillianforde.teensrunwestchester.data;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.teensrunwestchester.jillianforde.teensrunwestchester.Constants;
import com.teensrunwestchester.jillianforde.teensrunwestchester.PracticeListWrapper;
import com.teensrunwestchester.jillianforde.teensrunwestchester.takeattendance.AttendanceHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * This is all of the backend information that Parse needs to persist data.
 */
public class BackendUtil {
    public Date testOrder;
    /*
     IDs to represent success&failure with loggin in.
     */
    public static final int LOGIN_SUCCESS = 1;
    public static final int LOGIN_WRONG_CREDENTIALS = 2;
    public static final int LOGIN_FAILURE = 3;

    /*
     possible ParseExceptions in order to inform the user of what happened.
     */
    public static final int SERVER_ERROR = ParseException.INTERNAL_SERVER_ERROR;
    public static final int CONNECTION_FAILURE = ParseException.CONNECTION_FAILED;
    public static final int OBJECT_NOT_FOUND = ParseException.OBJECT_NOT_FOUND;
    public static final int TIMEOUT = ParseException.TIMEOUT;

    /*
     IDs to represent success&failure with saving
     */
    public static final int SAVE_SUCCESS = 0;
    public static final int SAVE_FAILURE = -1;
    /*
     IDs to represent success&failure with signing up
     */
    public static final int SIGNUP_FAILURE = -2;
    public static final int SIGNUP_SUCCESS = 1;
    /*
    IDs to represent success&failure with resetting password
     */
    public static final int RESET_PASSWORD_FAILURE = -3;
    public static final int RESET_PASSWORD_SUCCESS = 5;
    /*
    IDs to onRegisterClick user
     */
    public static final int REGISTER_SUCCESS = 4;
    /*
    ID to update changes to a user's profile(first name, last name, email or password)
     */
    public static final int UPDATE_PROFILE_SUCCESS = 6;
    /*
    exceptions related to user input
     */
    public static final int USER_EXISTS = ParseException.USERNAME_TAKEN;
    public static final int EMAIL_MISSING = ParseException.EMAIL_MISSING;
    public static final int EMAIL_TAKEN = ParseException.EMAIL_TAKEN;
    public static final int INCORRECT_INPUT_TYPE = ParseException.INCORRECT_TYPE;
    public static final int INVALID_EMAIL_ADDRESS = ParseException.INVALID_EMAIL_ADDRESS;
    public static final int USER_MISSING = ParseException.LINKED_ID_MISSING;
    public static final int PASSWORD_MISSING = ParseException.PASSWORD_MISSING;

    public static final int EMAIL_NOT_FOUND = ParseException.EMAIL_NOT_FOUND;

    public static void saveUserInformation(String firstName, String lastName, String email) {
        if (isUserLoggedIn()) {
            ParseUser user  = ParseUser.getCurrentUser();
            user.put(Constants.PARSE_USER_FIRSTNAME, firstName);
            user.put(Constants.PARSE_USER_LASTNAME, lastName);
            user.setEmail(email);
            user.saveEventually();
        }
    }

    public static String getUsername() {
        if (ParseUser.getCurrentUser() == null) {
            return "This user is not logged in";
        }
        return ParseUser.getCurrentUser().getUsername();
    }

    public static boolean isMentorStatus(){
        return ParseUser.getCurrentUser().getBoolean(Constants.PARSE_USER_ISMENTOR);
    }

    public static int loginWithEmail(String email, String password) {
        try {
            ParseUser parseUser = ParseUser.logIn(email, password);

        } catch (ParseException e) {
            e.printStackTrace();
            switch (e.getCode()) {
                case OBJECT_NOT_FOUND:
                    return LOGIN_WRONG_CREDENTIALS;
                default:
                    return LOGIN_FAILURE;
            }
        }

        return LOGIN_SUCCESS;
    }

    public static boolean isUserLoggedIn() {
        return ParseUser.getCurrentUser() != null;
    }

    //Retrieves the list of PracticeActivity objects that have not yet happened
    //a.k.a dueDate is greater than "now" and "translates" them into a PracticeActivityEvent list
    public static PracticeListWrapper getUpcomingPracticeEvents() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.PARSE_TABLE_PRACTICE);
        query.orderByAscending(Constants.PARSE_PRACTICE_DUE);
        Date date = new Date();
        query.whereGreaterThanOrEqualTo(Constants.PARSE_PRACTICE_DUE, date);

        //We need to explicitly tell parse to include the actual object with this query

        query.include(Constants.PARSE_PRACTICE_MENTORPOINTER);

        PracticeListWrapper wrapper = new PracticeListWrapper();
        List<PracticeActivityEvent> theList = new ArrayList<>();
        try {
            //find() makes a request to the Parse server to find the objects that fulfill
            //the query constraints
            List<ParseObject> objects = query.find();

            for (int i = 0; i < objects.size(); i++) {
                String objectId = objects.get(i).getObjectId();
                String name = objects.get(i).getString(Constants.PARSE_PRACTICE_NAME);
                String locationName = objects.get(i).getString(Constants.PARSE_PRACTICE_LOCATIONNAME);
                ParseGeoPoint location = objects.get(i).getParseGeoPoint(Constants.PARSE_PRACTICE_LOCATION);
                LatLng latlng = new LatLng( location.getLatitude(),location.getLongitude());
                String practiceTime = objects.get(i).getString(Constants.PARSE_PRACTICE_TIME);
                //This will return null if we forget to call query.include(Constants.PARSE_PRACTICE_MENTORPOINTER)
                ParseUser mentor = objects.get(i).getParseUser(Constants.PARSE_PRACTICE_MENTORPOINTER);
                String mentorName = mentor.getString(Constants.PARSE_USER_FIRSTNAME) + " " +
                        mentor.getString(Constants.PARSE_USER_LASTNAME);
                int milesRun = objects.get(i).getInt(Constants.PARSE_PRACTICE_MILESRUN);

                PracticeActivityEvent item = new PracticeActivityEvent(objectId, name,latlng,locationName, milesRun, mentorName, practiceTime);
                theList.add(item);
            }

            wrapper.theList = theList;

        } catch (ParseException e) {
            e.printStackTrace();
            wrapper.theList = null;
            wrapper.resultCode = e.getCode();
        }

        return wrapper;
    }

    public static PracticeListWrapper getMyPractices() {
        //This query will return only the practices that have not happened yet, ordered by date
        ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery(Constants.PARSE_TABLE_PRACTICE);
        innerQuery.whereGreaterThanOrEqualTo(Constants.PARSE_PRACTICE_DUE, new Date());
        //innerQuery.orderByAscending(Constants.PARSE_PRACTICE_DUE);

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.PARSE_TABLE_ATTENDANCEHISTORY);
        query.include(Constants.PARSE_ATTENDANCEHISTORY_PRACTICE);
        query.whereEqualTo(Constants.PARSE_ATTENDANCEHISTORY_USER, ParseUser.getCurrentUser());
        query.whereMatchesQuery(Constants.PARSE_ATTENDANCEHISTORY_PRACTICE, innerQuery);

        //this was whereGreaterThan before
        //query.whereGreaterThanOrEqualTo(Constants.PARSE_PRACTICE_DUE, date);

        PracticeListWrapper wrapper = new PracticeListWrapper();
        List<PracticeActivityEvent> theList = new ArrayList<>();
        try {
            //find() makes a request to the Parse server to find the objects that fulfill
            //the query constraints
            List<ParseObject> objects = query.find();
            Collections.sort(objects, new PracticeDateComp());
            for (int i = 0; i < objects.size(); i++) {
                    ParseObject practiceParseObject = objects.get(i).getParseObject(Constants.PARSE_ATTENDANCEHISTORY_PRACTICE);
                    String objectId = practiceParseObject.getObjectId();
                    String name = practiceParseObject.getString(Constants.PARSE_PRACTICE_NAME);
                    String locationName = practiceParseObject.getString(Constants.PARSE_PRACTICE_LOCATIONNAME);
                    ParseGeoPoint location = practiceParseObject.getParseGeoPoint(Constants.PARSE_PRACTICE_LOCATION);
                    LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                    String practiceTime = practiceParseObject.getString(Constants.PARSE_PRACTICE_TIME);
                    //This will return null if we forget to call query.include(Constants.PARSE_PRACTICE_MENTORPOINTER)
                    ParseUser mentor = practiceParseObject.getParseUser(Constants.PARSE_PRACTICE_MENTORPOINTER).fetchIfNeeded();
                    String mentorName = mentor.getString(Constants.PARSE_USER_FIRSTNAME) + " " +
                            mentor.getString(Constants.PARSE_USER_LASTNAME);
                    int milesRun = objects.get(i).getInt(Constants.PARSE_PRACTICE_MILESRUN);

                    PracticeActivityEvent item = new PracticeActivityEvent(objectId,
                            name, latlng, locationName, milesRun, mentorName, practiceTime);
                    theList.add(item);

            }
            wrapper.theList = theList;

        } catch (ParseException e) {
            e.printStackTrace();
            wrapper.theList = null;
            wrapper.resultCode = e.getCode();
        }

        return wrapper;
    }

    public static List<AttendanceHistory> getAttendanceHistoryForPractice(String practiceId) {
        ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery(Constants.PARSE_TABLE_PRACTICE);
        innerQuery.whereEqualTo(Constants.PARSE_PRACTICE_OBJECTID, practiceId);

        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.PARSE_TABLE_ATTENDANCEHISTORY);
        query.whereMatchesQuery(Constants.PARSE_ATTENDANCEHISTORY_PRACTICE, innerQuery);

        try {
            List<ParseObject> parseObjects = query.find();
            List<AttendanceHistory> theList = new ArrayList<>();

            for (int i = 0; i < parseObjects.size(); i++) {
                Log.d("BackendUtil", "An object");
                ParseObject practiceParseObject = parseObjects.get(i).getParseObject(Constants.PARSE_ATTENDANCEHISTORY_PRACTICE).fetchIfNeeded();
                ParseUser userParseObject = parseObjects.get(i).getParseUser(Constants.PARSE_ATTENDANCEHISTORY_USER).fetchIfNeeded();
                boolean didAttend = parseObjects.get(i).getBoolean(Constants.PARSE_ATTENDANCEHISTORY_DIDATTEND);
                String objectId = parseObjects.get(i).getObjectId();
                theList.add(new AttendanceHistory(objectId, fromParseObjectToPractice(practiceParseObject), fromParseUserToUser(userParseObject), didAttend));
            }
            return theList;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static PracticeActivityEvent fromParseObjectToPractice (ParseObject parseObject) throws ParseException {
        String id = parseObject.getObjectId();
        ParseGeoPoint parseGeoPoint = parseObject.getParseGeoPoint(Constants.PARSE_PRACTICE_LOCATION);
        LatLng location = new LatLng(parseGeoPoint.getLatitude(), parseGeoPoint.getLongitude());
        String locationName = parseObject.getString(Constants.PARSE_PRACTICE_LOCATIONNAME);
        Integer milesRun = parseObject.getInt(Constants.PARSE_PRACTICE_MILESRUN);
        ParseUser mentor = parseObject.getParseUser(Constants.PARSE_PRACTICE_MENTORPOINTER).fetchIfNeeded();
        String mentorName = mentor.getString(Constants.PARSE_USER_FIRSTNAME) + " " +
                mentor.getString(Constants.PARSE_USER_LASTNAME);
        String practiceTime = parseObject.getString(Constants.PARSE_PRACTICE_TIME);

        String practiceName = parseObject.getString(Constants.PARSE_PRACTICE_NAME);
        return new PracticeActivityEvent(id, practiceName, location, locationName, milesRun, mentorName, practiceTime);
    }

    private static User fromParseUserToUser(ParseUser parseUser) {
        String id = parseUser.getObjectId();
        String firstName = parseUser.getString(Constants.PARSE_USER_FIRSTNAME);
        String lastName = parseUser.getString(Constants.PARSE_USER_LASTNAME);
        String email = parseUser.getEmail();
        boolean isMentor = parseUser.getBoolean(Constants.PARSE_USER_ISMENTOR);

        return new User(id, firstName, lastName, email, isMentor);
    }

    public static int registerAttendance(String practiceId) {
        ParseObject attendanceHistory = new ParseObject(Constants.PARSE_TABLE_ATTENDANCEHISTORY);
        attendanceHistory.put(Constants.PARSE_ATTENDANCEHISTORY_PRACTICE,
                ParseObject.createWithoutData(Constants.PARSE_TABLE_PRACTICE, practiceId));
        attendanceHistory.put(Constants.PARSE_ATTENDANCEHISTORY_USER, ParseUser.getCurrentUser());
        attendanceHistory.put(Constants.PARSE_ATTENDANCEHISTORY_DIDATTEND, false);

        //ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.PARSE_TABLE_PRACTICE);
        //query.include(Constants.PARSE_PRACTICE_ATTENDINGUSERS);
        try {
            /*ParseObject upcomingPractice = query.get(practiceId);
            List<ParseUser> attendingUsers = (ArrayList<ParseUser>) upcomingPractice.get(Constants.PARSE_PRACTICE_ATTENDINGUSERS);

            if (attendingUsers == null) {
                //It means that the array has not been set
                attendingUsers = new ArrayList<ParseUser>();
            }

            attendingUsers.add(ParseUser.getCurrentUser());

            upcomingPractice.put(Constants.PARSE_PRACTICE_ATTENDINGUSERS, attendingUsers);
            upcomingPractice.save();*/
            attendanceHistory.save();
            return SAVE_SUCCESS;
        } catch (ParseException e) {
            e.printStackTrace();
            if (e.getCode() != ParseException.OBJECT_NOT_FOUND)
                return SAVE_FAILURE;
            else
                return OBJECT_NOT_FOUND;
        }
    }

    public static boolean isUserAttending(String practiceId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.PARSE_TABLE_ATTENDANCEHISTORY);
        query.whereEqualTo(Constants.PARSE_ATTENDANCEHISTORY_PRACTICE,
                ParseObject.createWithoutData(Constants.PARSE_TABLE_PRACTICE, practiceId));
        query.whereEqualTo(Constants.PARSE_ATTENDANCEHISTORY_USER, ParseUser.getCurrentUser());

        try {
            List<ParseObject> list = query.find();
            if (list == null || list.size() == 0)
                return false;
            else
                return true;

            /*ParseObject practiceObj = query.get(practiceId);
            List<ParseUser> attendingUsers = practiceObj.getList(Constants.PARSE_PRACTICE_ATTENDINGUSERS);

            if (attendingUsers == null) return false;
            ParseUser thisUser = ParseUser.getCurrentUser();
            for (ParseUser thatUser : attendingUsers) {
                if (thisUser.getObjectId().equals(thatUser.getObjectId())) {
                    return true;
                }
            }*/

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static int registerUser(String firstName, String lastName, String email, String password) {
        ParseUser newUser = new ParseUser();
        newUser.setUsername(email);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.put(Constants.PARSE_USER_FIRSTNAME, firstName);
        newUser.put(Constants.PARSE_USER_LASTNAME, lastName);
        try {
            newUser.signUp();
            return SIGNUP_SUCCESS;
        } catch (ParseException e) {
            e.printStackTrace();
            switch (e.getCode()) {
                case EMAIL_TAKEN:
                    return e.getCode();
                case USER_EXISTS:
                    return e.getCode();
                default:
                    return SIGNUP_FAILURE;
            }
        }
    }

    public static int cancelAttendance(String practiceId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.PARSE_TABLE_ATTENDANCEHISTORY);
        //Get the AttenanceHistory where the user is equals to the current user
        ParseQuery<ParseObject> attendanceQuery = ParseQuery.getQuery(Constants.PARSE_TABLE_PRACTICE);
        try{
            ParseObject practice = attendanceQuery.get(practiceId);


            query.whereEqualTo(Constants.PARSE_ATTENDANCEHISTORY_USER, ParseUser.getCurrentUser());

            //Get the AttendanceHistory where the Practice is equals to the one with practiceId
            Log.d(BackendUtil.class.getSimpleName(), practiceId);
            query.whereEqualTo(Constants.PARSE_ATTENDANCEHISTORY_PRACTICE, practice);

            /*ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.PARSE_TABLE_PRACTICE);
            query.include(Constants.PARSE_PRACTICE_ATTENDINGUSERS);
            */

            List<ParseObject> list = query.find();
            if (list != null && list.size()>0) {
                ParseObject object = list.get(0);
                object.delete();
            }

            return SAVE_SUCCESS;
        } catch (ParseException e) {
            e.printStackTrace();
            if (e.getCode() != ParseException.OBJECT_NOT_FOUND)
                return SAVE_FAILURE;
            else
                return OBJECT_NOT_FOUND;
        }
    }

    public static int resetPassword(String email) {
        try {
            ParseUser.requestPasswordReset(email);
            return RESET_PASSWORD_SUCCESS;
        } catch (ParseException e) {
            e.printStackTrace();
            switch(e.getCode()) {
                case ParseException.EMAIL_NOT_FOUND:
                    return EMAIL_NOT_FOUND;
                default:
                    return RESET_PASSWORD_FAILURE;
            }
        }

    }

    public static List<String> submitAttendance(List<AttendanceHistory> attendanceHistories) {
        List<String> confirmedRunners = new ArrayList<>();
        for (int i = 0; i < attendanceHistories.size(); i++) {
            String attendanceHistoryId = attendanceHistories.get(i).getObjectId();

            ParseObject parseObject = ParseObject.
                    createWithoutData(Constants.PARSE_TABLE_ATTENDANCEHISTORY, attendanceHistoryId);
            parseObject.put(Constants.PARSE_ATTENDANCEHISTORY_DIDATTEND, attendanceHistories.get(i).getUserAttended());

            Log.d("BackendUtil", "getUserAttended" + attendanceHistories.get(i).getUserAttended());
            try {
                parseObject.save();
                if (attendanceHistories.get(i).getUserAttended())
                    confirmedRunners.add(attendanceHistories.get(i).getUser().getFirstName() +
                            " " + attendanceHistories.get(i).getUser().getLastName());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return confirmedRunners;
    }


    public static void logOut(){
        ParseUser.getCurrentUser().logOut();
    }

    private static class PracticeDateComp implements Comparator<ParseObject> {

        @Override
        public int compare(ParseObject thisPractice, ParseObject thatPractice) {
            return thisPractice.getParseObject(Constants.PARSE_ATTENDANCEHISTORY_PRACTICE).getDate(Constants.PARSE_PRACTICE_DUE).
                    compareTo(thatPractice.getParseObject(Constants.PARSE_ATTENDANCEHISTORY_PRACTICE).getDate(Constants.PARSE_PRACTICE_DUE));
        }
    }

    public static int updateUserProfile(String firstName, String lastName, String email, String password){
        ParseUser user  = ParseUser.getCurrentUser();
        user.setUsername(email);
        user.put(Constants.PARSE_USER_FIRSTNAME, firstName);
        user.put(Constants.PARSE_USER_LASTNAME, lastName);
        user.setEmail(email);
        if(!password.isEmpty())
            user.setPassword(password);

        try {
            user.save();
            return UPDATE_PROFILE_SUCCESS;
        } catch (ParseException e) {
            e.printStackTrace();
            if (e.getCode() == ParseException.USERNAME_TAKEN)
                return EMAIL_TAKEN;
            return SAVE_FAILURE;
        }
    }

    public static User getCurrentUser() {
        if (ParseUser.getCurrentUser() == null)
            return null;

        return fromParseUserToUser(ParseUser.getCurrentUser());
    }
}

