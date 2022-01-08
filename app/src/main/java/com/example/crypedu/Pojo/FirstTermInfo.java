package com.example.crypedu.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by INDID on 09-01-2018.
 */

public class FirstTermInfo implements Parcelable {
    public String ft_class;
    public String ft_subject;
    public String ft_periodic;
    public String ft_note_book;
    public String ft_sub_enrich;
    public String ft_half_yearly;
    public String ft_marks_obtain;
    public String ft_grade;

    public FirstTermInfo(String ft_class, String ft_subject, String ft_periodic, String ft_note_book, String ft_sub_enrich, String ft_half_yearly, String ft_marks_obtain, String ft_grade) {
        this.ft_class = ft_class;
        this.ft_subject = ft_subject;
        this.ft_periodic = ft_periodic;
        this.ft_note_book = ft_note_book;
        this.ft_sub_enrich = ft_sub_enrich;
        this.ft_half_yearly = ft_half_yearly;
        this.ft_marks_obtain = ft_marks_obtain;
        this.ft_grade = ft_grade;
    }

    private FirstTermInfo(Parcel in) {
        ft_class = in.readString();
        ft_subject = in.readString();
        ft_periodic = in.readString();
        ft_note_book = in.readString();
        ft_sub_enrich = in.readString();
        ft_half_yearly = in.readString();
        ft_marks_obtain = in.readString();
        ft_grade = in.readString();
    }

    public static final Creator<FirstTermInfo> CREATOR = new Creator<FirstTermInfo>() {
        @Override
        public FirstTermInfo createFromParcel(Parcel in) {
            return new FirstTermInfo(in);
        }

        @Override
        public FirstTermInfo[] newArray(int size) {
            return new FirstTermInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ft_class);
        dest.writeString(ft_subject);
        dest.writeString(ft_periodic);
        dest.writeString(ft_note_book);
        dest.writeString(ft_sub_enrich);
        dest.writeString(ft_half_yearly);
        dest.writeString(ft_marks_obtain);
        dest.writeString(ft_grade);
    }
}
