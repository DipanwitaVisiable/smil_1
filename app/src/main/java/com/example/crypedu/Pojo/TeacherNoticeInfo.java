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

public class TeacherNoticeInfo implements Parcelable {
    public String noticeId;
    public String noticeSubject;
    public String noticeMessage;
    public String noticeClassName;
    public String noticeDate;
    public String noticeSection;

    public TeacherNoticeInfo(String noticeId, String noticeSubject, String noticeMessage,
                             String noticeClassName, String noticeDate, String noticeSection){
        this.noticeId = noticeId;
        this.noticeSubject = noticeSubject;
        this.noticeMessage = noticeMessage;
        this.noticeClassName = noticeClassName;
        this.noticeDate = noticeDate;
        this.noticeSection = noticeSection;
    }

    public TeacherNoticeInfo(Parcel in){
        this.noticeId = in.readString();
        this.noticeSubject = in.readString();
        this.noticeMessage = in.readString();
        this.noticeClassName = in.readString();
        this.noticeDate = in.readString();
        this.noticeSection = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(noticeId);
        dest.writeString(noticeSubject);
        dest.writeString(noticeMessage);
        dest.writeString(noticeClassName);
        dest.writeString(noticeDate);
        dest.writeString(noticeSection);
    }

    public static final Creator<TeacherNoticeInfo> CREATOR = new Creator<TeacherNoticeInfo>() {
        public TeacherNoticeInfo createFromParcel(Parcel in) {
            return new TeacherNoticeInfo(in);
        }

        public TeacherNoticeInfo[] newArray(int size) {
            return new TeacherNoticeInfo[size];

        }
    };
}

