package com.example.crypedu.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hp on 6/21/2017.
 */
public class NotificationInfo implements Parcelable {
    public String totalNo;
    public String name;

    public NotificationInfo(String name, String totalNo){
        this.name = name;
        this.totalNo = totalNo;
    }
    private NotificationInfo(Parcel in) {
        this.name = in.readString();
        this.totalNo = in.readString();
    }

    public static final Creator<NotificationInfo> CREATOR = new Creator<NotificationInfo>() {
        @Override
        public NotificationInfo createFromParcel(Parcel in) {
            return new NotificationInfo(in);
        }

        @Override
        public NotificationInfo[] newArray(int size) {
            return new NotificationInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(totalNo);
    }
}
