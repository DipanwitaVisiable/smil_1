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

public class TeacherHomeWorkInfo implements Parcelable {
    public String homeWorkId;
    public String homeWorkSubject;
    public String homeWorkTopic;
    public String className;
    public String section;
    public String fromDate;
    public String toDate;

    public TeacherHomeWorkInfo(String homeWorkId, String homeWorkSubject, String homeWorkTopic,
                                String className, String section, String fromDate, String toDate){
        this.homeWorkId = homeWorkId;
        this.homeWorkSubject = homeWorkSubject;
        this.homeWorkTopic = homeWorkTopic;
        this.className = className;
        this.section = section;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public TeacherHomeWorkInfo(Parcel in){
        this.homeWorkId = in.readString();
        this.homeWorkSubject = in.readString();
        this.homeWorkTopic = in.readString();
        this.className = in.readString();
        this.section = in.readString();
        this.fromDate = in.readString();
        this.toDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(homeWorkId);
        dest.writeString(homeWorkSubject);
        dest.writeString(homeWorkTopic);
        dest.writeString(className);
        dest.writeString(section);
        dest.writeString(fromDate);
        dest.writeString(toDate);
    }

    public static final Creator<TeacherHomeWorkInfo> CREATOR = new Creator<TeacherHomeWorkInfo>() {
        public TeacherHomeWorkInfo createFromParcel(Parcel in) {
            return new TeacherHomeWorkInfo(in);
        }

        public TeacherHomeWorkInfo[] newArray(int size) {
            return new TeacherHomeWorkInfo[size];

        }
    };
}

