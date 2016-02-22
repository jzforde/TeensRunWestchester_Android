package com.teensrunwestchester.jillianforde.teensrunwestchester.takeattendance;

import com.teensrunwestchester.jillianforde.teensrunwestchester.data.User;

/**
 * Created by jillianforde on 2/22/16.
 */
public interface UserAttendance {

    User getUser();
    void setUser(User mUser);

    boolean getUserAttended();
    void setUserAttended(boolean attended);
}
