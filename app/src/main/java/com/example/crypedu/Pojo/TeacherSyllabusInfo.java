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

public class TeacherSyllabusInfo implements Parcelable {
    public String syllabusId;
    public String syllabusSubject;
    public String syllabusChapter;
    public String syllabusClassName;
    public String syllabusSection;
    public String syllabusNoOfClass;

    public TeacherSyllabusInfo(String syllabusId, String syllabusSubject, String syllabusChapter,
                               String syllabusClassName, String syllabusSection, String syllabusNoOfClass){
        this.syllabusId = syllabusId;
        this.syllabusSubject = syllabusSubject;
        this.syllabusChapter = syllabusChapter;
        this.syllabusClassName = syllabusClassName;
        this.syllabusSection = syllabusSection;
        this.syllabusNoOfClass = syllabusNoOfClass;
    }

    public TeacherSyllabusInfo(Parcel in){
        this.syllabusId = in.readString();
        this.syllabusSubject = in.readString();
        this.syllabusChapter = in.readString();
        this.syllabusClassName = in.readString();
        this.syllabusSection = in.readString();
        this.syllabusNoOfClass = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(syllabusId);
        dest.writeString(syllabusSubject);
        dest.writeString(syllabusChapter);
        dest.writeString(syllabusClassName);
        dest.writeString(syllabusSection);
        dest.writeString(syllabusNoOfClass);
    }

    public static final Creator<TeacherSyllabusInfo> CREATOR = new Creator<TeacherSyllabusInfo>() {
        public TeacherSyllabusInfo createFromParcel(Parcel in) {
            return new TeacherSyllabusInfo(in);
        }

        public TeacherSyllabusInfo[] newArray(int size) {
            return new TeacherSyllabusInfo[size];

        }
    };
}


