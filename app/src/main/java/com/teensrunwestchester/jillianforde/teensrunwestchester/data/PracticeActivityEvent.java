package com.teensrunwestchester.jillianforde.teensrunwestchester.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jillianforde on 11/1/15.
 * Model for Activity
 */
public class PracticeActivityEvent implements Parcelable {

    private String mId;
    private String mName;
    private LatLng mLocation;
    private String mLocationName;
    private Integer mMilesRun;
    private String mMentorName;
    private String mPracticeTime;

    //TODO: TRYING TO CHECK IF USER IS A MENTOR
    public String getmPracticeTime() {
        return mPracticeTime;
    }

    public void setmPracticeTime(String mPracticeTime) {
        this.mPracticeTime = mPracticeTime;
    }

    public String getmMentorName() {
        return mMentorName;
    }

    public void setmMentorName(String mMentorName) {
        this.mMentorName = mMentorName;
    }

    public Integer getmMilesRun() {
        return mMilesRun;
    }

    public void setmMilesRun(Integer mMilesRun) {
        this.mMilesRun = mMilesRun;
    }

    public static Creator<PracticeActivityEvent> CREATOR = new Creator<PracticeActivityEvent>() {
        @Override
        public PracticeActivityEvent createFromParcel(Parcel source) {
            return new PracticeActivityEvent(source);
        }

        @Override
        public PracticeActivityEvent[] newArray(int size) {
            return new PracticeActivityEvent[size];
        }
    };

    private PracticeActivityEvent(Parcel src) {
        setId(src.readString());
        setName(src.readString());
        setLocation((LatLng) src.readParcelable(LatLng.class.getClassLoader()));
        setmLocationName(src.readString());
        setmMilesRun(src.readInt());
        setmMentorName(src.readString());
        setmPracticeTime(src.readString());
    }

    public String getmLocationName() {
        return mLocationName;
    }

    public void setmLocationName(String mLocationName) {
        this.mLocationName = mLocationName;
    }

    public PracticeActivityEvent(String objectId, String name, LatLng location, String locationName, Integer milesRun, String mentorName, String practiceTime) {
        setId(objectId);
        setName(name);
        setLocation(location);
        setmLocationName(locationName);
        setmMilesRun(milesRun);
        setmMentorName(mentorName);
        setmPracticeTime(practiceTime);

    }
    public LatLng getLocation(){
        return mLocation;
    }

    private void setLocation(LatLng location) {
        this.mLocation = location;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeParcelable(mLocation, flags);
        dest.writeString(mLocationName);
        dest.writeInt(mMilesRun);
        dest.writeString(mMentorName);
        dest.writeString(mPracticeTime);
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }
}
