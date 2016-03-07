package com.teensrunwestchester.jillianforde.teensrunwestchester.takeattendance;

import com.teensrunwestchester.jillianforde.teensrunwestchester.data.User;

import java.util.Date;

/**
 * Created by jillianforde on 2/22/16.
 */
public interface UserAttendance {

    User getUser();
    void setUser(User mUser);

    boolean getUserAttended();
    void setUserAttended(boolean attended);

    void setAttendingDate(Date value);
    Date getAttendingDate();

    void setCancelAttendanceStatus(boolean status);
    boolean getCancelAttendanceStatus();

}
