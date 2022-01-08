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

public class TeacherClassWorkInfo implements Parcelable {
    public String classWorkId;
    public String classWorkSubject;
    public String classWorkTopic;
    public String className;
    public String section;
    public String dateOfClass;

    public TeacherClassWorkInfo(String classWorkId, String classWorkSubject, String classWorkTopic,
                                String className, String section, String dateOfClass){
        this.classWorkId = classWorkId;
        this.classWorkSubject = classWorkSubject;
        this.classWorkTopic = classWorkTopic;
        this.className = className;
        this.section = section;
        this.dateOfClass = dateOfClass;
    }

    public TeacherClassWorkInfo(Parcel in){
        this.classWorkId = in.readString();
        this.classWorkSubject = in.readString();
        this.classWorkTopic = in.readString();
        this.className = in.readString();
        this.section = in.readString();
        this.dateOfClass = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(classWorkId);
        dest.writeString(classWorkSubject);
        dest.writeString(classWorkTopic);
        dest.writeString(className);
        dest.writeString(section);
        dest.writeString(dateOfClass);
    }

    public static final Creator<TeacherClassWorkInfo> CREATOR = new Creator<TeacherClassWorkInfo>() {
        public TeacherClassWorkInfo createFromParcel(Parcel in) {
            return new TeacherClassWorkInfo(in);
        }

        public TeacherClassWorkInfo[] newArray(int size) {
            return new TeacherClassWorkInfo[size];

        }
    };
}

