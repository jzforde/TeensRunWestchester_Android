package com.teensrunwestchester.jillianforde.teensrunwestchester.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jillianforde on 12/7/15.
 */
public class AttendanceHistory implements Parcelable {
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
        setmUser(source.<User>readParcelable(User.class.getClassLoader()));
        setDidAttend(source.readString().equals("true"));
        setObjectId(source.readString());
    }

    public AttendanceHistory(String objectId, PracticeActivityEvent practiceActivityEvent, User user, boolean didAttend) {
        setObjectId(objectId);
        setmPracticeActivityEvent(practiceActivityEvent);
        setmUser(user);
        setDidAttend(didAttend);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(getmUser(), flags);
        dest.writeParcelable(getmPracticeActivityEvent(), flags);
        dest.writeString(mDidAttend? "true" : "false");
        dest.writeString(mObjectId);
    }

    public User getmUser() {
        return mUser;
    }

    public void setmUser(User mUser) {
        this.mUser = mUser;
    }

    public PracticeActivityEvent getmPracticeActivityEvent() {
        return mPracticeActivityEvent;
    }

    public void setmPracticeActivityEvent(PracticeActivityEvent mPracticeActivityEvent) {
        this.mPracticeActivityEvent = mPracticeActivityEvent;
    }

    public boolean didUserAttend() {
        return mDidAttend;
    }

    public void setDidAttend(boolean didAttend) {
        mDidAttend = didAttend;
    }

    public String getObjectId() {
        return mObjectId;
    }

    public void setObjectId(String id) {
        mObjectId = id;
    }
}
