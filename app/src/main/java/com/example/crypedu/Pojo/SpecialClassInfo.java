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

public class SpecialClassInfo implements Parcelable {
    public String time;
    public String subject6;

    public SpecialClassInfo(String time, String subject6){
        this.time = time;
        this.subject6 = subject6;
    }

    private SpecialClassInfo(Parcel in){
        this.time = in.readString();
        this.subject6 = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeString(subject6);
    }

    public static final Creator<SpecialClassInfo> CREATOR = new Creator<SpecialClassInfo>() {
        public SpecialClassInfo createFromParcel(Parcel in) {
            return new SpecialClassInfo(in);
        }

        public SpecialClassInfo[] newArray(int size) {
            return new SpecialClassInfo[size];

        }
    };
}