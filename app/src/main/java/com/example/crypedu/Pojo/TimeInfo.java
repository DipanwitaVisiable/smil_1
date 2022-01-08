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

public class TimeInfo implements Parcelable {
    public String time;
    public String subject1;
    public String subject2;
    public String subject3;
    public String subject4;
    public String subject5;
    public String subject6;

    public TimeInfo(String time, String subject1, String subject2, String subject3, String subject4, String subject5, String subject6){
        this.time = time;
        this.subject1 = subject1;
        this.subject2 = subject2;
        this.subject3 = subject3;
        this.subject4 = subject4;
        this.subject5 = subject5;
        this.subject6 = subject6;
    }

    public TimeInfo(Parcel in){
        this.time = in.readString();
        this.subject1 = in.readString();
        this.subject2 = in.readString();
        this.subject3 = in.readString();
        this.subject4 = in.readString();
        this.subject5 = in.readString();
        this.subject6 = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeString(subject1);
        dest.writeString(subject2);
        dest.writeString(subject3);
        dest.writeString(subject4);
        dest.writeString(subject5);
        dest.writeString(subject6);
    }

    public static final Creator<TimeInfo> CREATOR = new Creator<TimeInfo>() {
        public TimeInfo createFromParcel(Parcel in) {
            return new TimeInfo(in);
        }

        public TimeInfo[] newArray(int size) {
            return new TimeInfo[size];

        }
    };
}

