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

public class TimeTableInfo implements Parcelable {
    public String time;
    public String monSub;
    public String tueSub;
    public String wedSub;
    public String thuSub;
    public String friSub;
    public String satSub;

    public TimeTableInfo(String time, String monSub, String tueSub, String wedSub, String thuSub,
                    String friSub, String satSub){
        this.time = time;
        this.monSub = monSub;
        this.tueSub = tueSub;
        this.wedSub = wedSub;
        this.thuSub = thuSub;
        this.friSub = friSub;
        this.satSub = satSub;
    }

    public TimeTableInfo(Parcel in){
        this.time = in.readString();
        this.monSub = in.readString();
        this.tueSub = in.readString();
        this.wedSub = in.readString();
        this.thuSub = in.readString();
        this.friSub = in.readString();
        this.satSub = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeString(monSub);
        dest.writeString(tueSub);
        dest.writeString(wedSub);
        dest.writeString(thuSub);
        dest.writeString(friSub);
        dest.writeString(satSub);
    }

    public static final Creator<TimeTableInfo> CREATOR = new Creator<TimeTableInfo>() {
        public TimeTableInfo createFromParcel(Parcel in) {
            return new TimeTableInfo(in);
        }

        public TimeTableInfo[] newArray(int size) {
            return new TimeTableInfo[size];

        }
    };
}
