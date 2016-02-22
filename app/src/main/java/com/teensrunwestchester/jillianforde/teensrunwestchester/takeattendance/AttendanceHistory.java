package com.teensrunwestchester.jillianforde.teensrunwestchester.takeattendance;

import android.os.Parcel;
import android.os.Parcelable;

import com.teensrunwestchester.jillianforde.teensrunwestchester.data.PracticeActivityEvent;
import com.teensrunwestchester.jillianforde.teensrunwestchester.data.User;

/**
 * Created by jillianforde on 12/7/15.
 */
public class AttendanceHistory implements Parcelable, UserAttendance {
    private User mUser;
    private PracticeActivityEvent mPracticeActivityEvent;
    private boolean mDidAttend;
    private String mObjectId;

    //creator is responsible for reading inside of your parcelable
    public static final Creator<AttendanceHistory> CREATOR = new Creator<AttendanceHistory>() {
        @Override
        public AttendanceHistory createFromParcel(Parcel source) {
            return new AttendanceHistory(source);
        }

        @Override
        public AttendanceHistory[] newArray(int size) {
            return new AttendanceHistory[size];
        }
    };

    private AttendanceHistory(Parcel source) {
        setmPracticeActivityEvent(source.<PracticeActivityEvent>readParcelable(PracticeActivityEvent.class.getClassLoader()));
        setUser(source.<User>readParcelable(User.class.getClassLoader()));
        setUserAttended(source.readString().equals("true"));
        setObjectId(source.readString());
    }

    public AttendanceHistory(String objectId, PracticeActivityEvent practiceActivityEvent, User user, boolean didAttend) {
        setObjectId(objectId);
        setmPracticeActivityEvent(practiceActivityEvent);
        setUser(user);
        setUserAttended(didAttend);
    }
    @Override
    public int describeContents() {
        // TODO why is returning always 0?
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(getUser(), flags);
        dest.writeParcelable(getmPracticeActivityEvent(), flags);
        dest.writeString(mDidAttend? "true" : "false");
        dest.writeString(mObjectId);
    }





    public PracticeActivityEvent getmPracticeActivityEvent() {
        return mPracticeActivityEvent;
    }

    public void setmPracticeActivityEvent(PracticeActivityEvent mPracticeActivityEvent) {
        this.mPracticeActivityEvent = mPracticeActivityEvent;
    }

    // UserAttendance implementation
    public User getUser() {
        return mUser;
    }
    public void setUser(User mUser) {
        this.mUser = mUser;
    }

    public boolean getUserAttended() {
        return mDidAttend;
    }

    public void setUserAttended(boolean didAttend) {
        mDidAttend = didAttend;
    }
    // END UserAttendance implementation

    // TODO clarify what is this object and change the method name
    //  to make clear what's going on
    public String getObjectId() {
        return mObjectId;
    }

    public void setObjectId(String id) {
        mObjectId = id;
    }
}
