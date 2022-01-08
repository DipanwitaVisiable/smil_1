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

public class SubjectInfo implements Parcelable {
    public String subjectId;
    public String subjectName;

    public SubjectInfo(String subjectId, String subjectName){
        this.subjectId = subjectId;
        this.subjectName = subjectName;
    }

    public SubjectInfo(Parcel in){
        this.subjectId = in.readString();
        this.subjectName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subjectId);
        dest.writeString(subjectName);
    }

    public static final Creator<SubjectInfo> CREATOR = new Creator<SubjectInfo>() {
        public SubjectInfo createFromParcel(Parcel in) {
            return new SubjectInfo(in);
        }

        public SubjectInfo[] newArray(int size) {
            return new SubjectInfo[size];

        }
    };
}
