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

public class SyllabusPlanningInfo implements Parcelable {
    public String id;
    public String class_name;
    public String teachers_name;
    public String subject;
    public String chapter;
    public String no_of_class;

    public SyllabusPlanningInfo(String id, String class_name, String teachers_name, String subject, String chapter,
                                String no_of_class){
        this.id = id;
        this.class_name = class_name;
        this.teachers_name = teachers_name;
        this.subject = subject;
        this.chapter = chapter;
        this.no_of_class = no_of_class;
    }

    public SyllabusPlanningInfo(Parcel in){
        this.id = in.readString();
        this.class_name = in.readString();
        this.teachers_name = in.readString();
        this.subject = in.readString();
        this.chapter = in.readString();
        this.no_of_class = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(class_name);
        dest.writeString(teachers_name);
        dest.writeString(subject);
        dest.writeString(chapter);
        dest.writeString(no_of_class);
    }

    public static final Creator<SyllabusPlanningInfo> CREATOR = new Creator<SyllabusPlanningInfo>() {
        public SyllabusPlanningInfo createFromParcel(Parcel in) {
            return new SyllabusPlanningInfo(in);
        }

        public SyllabusPlanningInfo[] newArray(int size) {
            return new SyllabusPlanningInfo[size];

        }
    };
}
