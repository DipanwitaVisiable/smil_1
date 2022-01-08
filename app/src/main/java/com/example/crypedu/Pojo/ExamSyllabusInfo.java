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

public class ExamSyllabusInfo implements Parcelable {
    public String chapter;
    public String no_of_class;

    public ExamSyllabusInfo(String chapter, String no_of_class){
        this.chapter = chapter;
        this.no_of_class = no_of_class;
    }

    private ExamSyllabusInfo(Parcel in){
        this.chapter = in.readString();
        this.no_of_class = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(chapter);
        dest.writeString(no_of_class);
    }

    public static final Creator<ExamSyllabusInfo> CREATOR = new Creator<ExamSyllabusInfo>() {
        public ExamSyllabusInfo createFromParcel(Parcel in) {
            return new ExamSyllabusInfo(in);
        }

        public ExamSyllabusInfo[] newArray(int size) {
            return new ExamSyllabusInfo[size];

        }
    };
}

