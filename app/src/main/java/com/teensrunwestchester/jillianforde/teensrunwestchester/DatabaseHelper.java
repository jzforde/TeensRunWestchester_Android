package com.teensrunwestchester.jillianforde.teensrunwestchester;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jillianforde on 11/3/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "trw.db";
    private static final int VERSION = 2;
    //parameters for all tables
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FIRST_NAME = "firstName";
    private static final String COLUMN_LAST_NAME = "lastName";

    //parameters for attendance table
    private static final String TABLE_NAME_ATTENDANCE = "attdendance";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_ATTEND_PRACTICE = "attendedPractice";

    //helper string to add a user's attendance to practice to the database
    private static final String ATTEND_PRACTICE = "INSERT INTO ATTENDANCE(id, date, firstName, lastName, attendedPractice ";

    //parameters for user table table
    private static final String TABLE_NAME_USER = "user";
    private static final String COLUMN_PHONE = "phoneNumber";
    private static final String COLUMN_EMAIL = "email";

    //helper string to create a user in the database
    private static final String CREATE_USER = "INSERT INTO USER(id, firstName, lastName, phoneNumber, email ";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //Will be caled if the database doesnt exist in your filesystem
        String createAttendanceQuery = "CREATE TABLE " + TABLE_NAME_ATTENDANCE + "("
                + COLUMN_ID + "PRIMARY KEY," +
                COLUMN_FIRST_NAME + "TEXT NOT NULL," +
                COLUMN_LAST_NAME + "TEXT NOT NULL," +
                COLUMN_ATTEND_PRACTICE + "TEXT NOT NULL," +
                COLUMN_DATE + "TEXT NOT NULL" + ");";
        db.execSQL(createAttendanceQuery);

        String createUserQuery = "CREATE TABLE " + TABLE_NAME_USER + "(" +
                COLUMN_ID + "PRIMARY KEY," +
                COLUMN_FIRST_NAME + "TEXT NOT NULL," +
                COLUMN_LAST_NAME + "TEXT NOT NULL," +
                COLUMN_PHONE + "TEXT NOT NULL," +
                COLUMN_EMAIL + "TEXT NOT NULL" + ");";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //DELETE the tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
        onCreate(db);
    }

}
