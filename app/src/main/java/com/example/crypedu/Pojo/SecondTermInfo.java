package com.example.crypedu.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by INDID on 09-01-2018.
 */

public class SecondTermInfo implements Parcelable {

    public String sd_subject;
    public String sd_periodic;
    public String sd_note_book;
    public String sd_sub_enrich;
    public String sd_yearly;
    public String sd_marks_obtain;
    public String sd_grade;

    public SecondTermInfo(String sd_subject, String sd_periodic, String sd_note_book, String sd_sub_enrich, String sd_yearly, String sd_marks_obtain, String sd_grade) {
        this.sd_subject = sd_subject;
        this.sd_periodic = sd_periodic;
        this.sd_note_book = sd_note_book;
        this.sd_sub_enrich = sd_sub_enrich;
        this.sd_yearly = sd_yearly;
        this.sd_marks_obtain = sd_marks_obtain;
        this.sd_grade = sd_grade;
    }


    private SecondTermInfo(Parcel in) {
        sd_subject = in.readString();
        sd_periodic = in.readString();
        sd_note_book = in.readString();
        sd_sub_enrich = in.readString();
        sd_yearly = in.readString();
        sd_marks_obtain = in.readString();
        sd_grade = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sd_subject);
        dest.writeString(sd_periodic);
        dest.writeString(sd_note_book);
        dest.writeString(sd_sub_enrich);
        dest.writeString(sd_yearly);
        dest.writeString(sd_marks_obtain);
        dest.writeString(sd_grade);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SecondTermInfo> CREATOR = new Creator<SecondTermInfo>() {
        @Override
        public SecondTermInfo createFromParcel(Parcel in) {
            return new SecondTermInfo(in);
        }

        @Override
        public SecondTermInfo[] newArray(int size) {
            return new SecondTermInfo[size];
        }
    };
}
