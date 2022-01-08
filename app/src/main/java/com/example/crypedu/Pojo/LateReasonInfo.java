package com.example.crypedu.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hp on 6/23/2017.
 */
public class LateReasonInfo implements Parcelable {
    public String lateReason;
    public String attendanceStatus;
    public String date;

    public LateReasonInfo(String lateReason, String attendanceStatus, String date){
        this.lateReason = lateReason;
        this.attendanceStatus = attendanceStatus;
        this.date = date;
    }
    private LateReasonInfo(Parcel in) {
        this.lateReason = in.readString();
        this.attendanceStatus = in.readString();
        this.date = in.readString();
    }

    public static final Creator<LateReasonInfo> CREATOR = new Creator<LateReasonInfo>() {
        @Override
        public LateReasonInfo createFromParcel(Parcel in) {
            return new LateReasonInfo(in);
        }

        @Override
        public LateReasonInfo[] newArray(int size) {
            return new LateReasonInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lateReason);
        dest.writeString(attendanceStatus);
        dest.writeString(date);
    }
}
