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

public class AttendanceInfo implements Parcelable {
    public String id;
    private String std_id;
    public String active;
    public String attendence_status;
    public String late_reason;
    public String date;

    public AttendanceInfo(String id, String std_id, String attendence_status, String date, String late_reason){
        this.id = id;
        this.std_id = std_id;
        this.attendence_status = attendence_status;
        this.late_reason = late_reason;
        this.date = date;
    }

    private AttendanceInfo(Parcel in){
        this.id = in.readString();
        this.std_id = in.readString();
        this.attendence_status = in.readString();
        this.late_reason = in.readString();
        this.date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(std_id);
        dest.writeString(attendence_status);
        dest.writeString(late_reason);
        dest.writeString(date);
    }

    public static final Creator<AttendanceInfo> CREATOR = new Creator<AttendanceInfo>() {
        public AttendanceInfo createFromParcel(Parcel in) {
            return new AttendanceInfo(in);
        }

        public AttendanceInfo[] newArray(int size) {
            return new AttendanceInfo[size];

        }
    };
}
