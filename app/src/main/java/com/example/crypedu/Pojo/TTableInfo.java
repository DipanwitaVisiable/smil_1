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

public class TTableInfo implements Parcelable {
    public String startTime;
    public String endTime;
    public String timeId;
    public String day;
    public String subject;

    public TTableInfo(String startTime, String endTime, String timeId, String day, String subject){
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeId = timeId;
        this.day = day;
        this.subject = subject;
    }

    public TTableInfo(Parcel in){
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.timeId = in.readString();
        this.day = in.readString();
        this.subject = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(timeId);
        dest.writeString(day);
        dest.writeString(subject);
    }

    public static final Creator<TTableInfo> CREATOR = new Creator<TTableInfo>() {
        public TTableInfo createFromParcel(Parcel in) {
            return new TTableInfo(in);
        }

        public TTableInfo[] newArray(int size) {
            return new TTableInfo[size];

        }
    };
}


