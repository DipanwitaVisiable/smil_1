package com.example.crypedu.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class BusInfo implements Parcelable {
    public String busNo, busLat, busLong, busStatus, busId;


    public BusInfo(String busNo, String busLat, String busLong, String busStatus, String busId){

        this.busNo = busNo;
        this.busLat = busLat;
        this.busLong = busLong;
        this.busStatus = busStatus;
        this.busId = busId;
    }

    private BusInfo(Parcel in) {
        busNo = in.readString();
        busLat = in.readString();
        busLong = in.readString();
        busStatus = in.readString();
        busId = in.readString();
    }

    public static final Creator<BusInfo> CREATOR = new Creator<BusInfo>() {
        @Override
        public BusInfo createFromParcel(Parcel in) {
            return new BusInfo(in);
        }

        @Override
        public BusInfo[] newArray(int size) {
            return new BusInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(busNo);
        dest.writeString(busLat);
        dest.writeString(busLong);
        dest.writeString(busStatus);
        dest.writeString(busId);
    }
}
