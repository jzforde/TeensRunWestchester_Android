package com.teensrunwestchester.jillianforde.teensrunwestchester.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jillianforde on 11/4/15.
 *
 * Model of a user object which identifies the characteristics of a user
 */
public class User implements Parcelable {
    private String firstName;
    private String lastName;
    private String id;
    private String email;
    private boolean isMentor;

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel source) {
        setFirstName(source.readString());
        setLastName(source.readString());
        setId(source.readString());
        setEmail(source.readString());
    }

    public User(String id, String firstName, String lastName, String email, boolean isMentor) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setIsMentor(isMentor);
    }

    public boolean getMentorStatus(){
        return isMentor();
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastNameVal) {
        this.lastName = lastNameVal;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (getId() != user.getId()) return false;
        if (!getFirstName().equals(user.getFirstName())) return false;
        if (!getLastName().equals(user.getLastName())) return false;
        return getEmail().equals(user.getEmail());
    }

    @Override
    public int hashCode() {
        int result = getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = getId().hashCode();
        result = 31 * result + getEmail().hashCode();
        return result;
    }

    public void setId(String idVal) {
        this.id = idVal;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String emailVal) {
        this.email = emailVal;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstNameVal) {
        this.firstName = firstNameVal;
    }


    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", id=" + id +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(id);
        dest.writeString(email);
    }

    public boolean isMentor() {
        return isMentor;
    }

    public void setIsMentor(boolean isMentor) {
        this.isMentor = isMentor;
    }
}
