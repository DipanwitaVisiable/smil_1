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

public class ExamInfo implements Parcelable {
    public String id;
    public String name;

    public ExamInfo(String id, String name){
        this.id = id;
        this.name = name;
    }

    private ExamInfo(Parcel in){
        this.id = in.readString();
        this.name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    public static final Creator<ExamInfo> CREATOR = new Creator<ExamInfo>() {
        public ExamInfo createFromParcel(Parcel in) {
            return new ExamInfo(in);
        }

        public ExamInfo[] newArray(int size) {
            return new ExamInfo[size];

        }
    };
}
