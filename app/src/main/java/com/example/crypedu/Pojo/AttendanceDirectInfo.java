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

public class AttendanceDirectInfo implements Parcelable {
    public String classSt;
    public String sectionSt;
    public String totalSt;
    public String totalPresent;
    public String percentage;

    public AttendanceDirectInfo(String classSt, String sectionSt, String totalSt, String totalPresent, String percentage){
        this.classSt = classSt;
        this.sectionSt = sectionSt;
        this.totalSt = totalSt;
        this.totalPresent = totalPresent;
        this.percentage = percentage;
    }

    private AttendanceDirectInfo(Parcel in){
        this.classSt = in.readString();
        this.sectionSt = in.readString();
        this.totalSt = in.readString();
        this.totalPresent = in.readString();
        this.percentage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(classSt);
        dest.writeString(sectionSt);
        dest.writeString(totalSt);
        dest.writeString(totalPresent);
        dest.writeString(percentage);
    }

    public static final Creator<AttendanceDirectInfo> CREATOR = new Creator<AttendanceDirectInfo>() {
        public AttendanceDirectInfo createFromParcel(Parcel in) {
            return new AttendanceDirectInfo(in);
        }

        public AttendanceDirectInfo[] newArray(int size) {
            return new AttendanceDirectInfo[size];

        }
    };
}
