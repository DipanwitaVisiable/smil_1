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

public class SyllabusSubjectInfo implements Parcelable {
    public String id;
    public String class_id;
    public String subject_name;

    public SyllabusSubjectInfo(String id, String class_id, String subject_name){
        this.id = id;
        this.class_id = class_id;
        this.subject_name = subject_name;
    }

    public SyllabusSubjectInfo(Parcel in){
        this.id = in.readString();
        this.class_id = in.readString();
        this.subject_name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(class_id);
        dest.writeString(subject_name);
    }

    public static final Creator<SyllabusSubjectInfo> CREATOR = new Creator<SyllabusSubjectInfo>() {
        public SyllabusSubjectInfo createFromParcel(Parcel in) {
            return new SyllabusSubjectInfo(in);
        }

        public SyllabusSubjectInfo[] newArray(int size) {
            return new SyllabusSubjectInfo[size];

        }
    };
}

