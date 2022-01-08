package com.example.crypedu.Pojo;
/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */
import android.os.Parcel;
import android.os.Parcelable;

public class SwitchChildInfo implements Parcelable {
    public String studentCode;
    public String fName;
    public String lName;
    public String studentId;
    public String user_rating;

    public SwitchChildInfo(String studentCode, String fName, String lName, String studentId, String user_rating){
        this.studentCode = studentCode;
        this.fName = fName;
        this.lName = lName;
        this.studentId = studentId;
        this.user_rating = user_rating;
    }

    public SwitchChildInfo(Parcel in){
        this.studentCode = in.readString();
        this.fName = in.readString();
        this.lName = in.readString();
        this.studentId = in.readString();
        this.user_rating = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(studentCode);
        dest.writeString(fName);
        dest.writeString(lName);
        dest.writeString(studentId);
        dest.writeString(user_rating);
    }

    public static final Creator<SwitchChildInfo> CREATOR = new Creator<SwitchChildInfo>() {
        public SwitchChildInfo createFromParcel(Parcel in) {
            return new SwitchChildInfo(in);
        }

        public SwitchChildInfo[] newArray(int size) {
            return new SwitchChildInfo[size];

        }
    };
}


