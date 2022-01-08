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

public class NoticeInfo implements Parcelable {
    public static final Creator<NoticeInfo> CREATOR = new Creator<NoticeInfo>() {
        public NoticeInfo createFromParcel(Parcel in) {
            return new NoticeInfo(in);
        }

        public NoticeInfo[] newArray(int size) {
            return new NoticeInfo[size];

        }
    };
    private String noticeClass;
    public String subject;
    public String date;
    public String notice;
    public String notice_pdf;
    public String notice_type;

    public NoticeInfo(String noticeClass, String subject, String date, String notice, String notice_pdf, String notice_type) {
        this.noticeClass = noticeClass;
        this.subject = subject;
        this.date = date;
        this.notice = notice;
        this.notice_pdf = notice_pdf;
        this.notice_type = notice_type;
    }

    private NoticeInfo(Parcel in) {
        this.noticeClass = in.readString();
        this.subject = in.readString();
        this.date = in.readString();
        this.notice = in.readString();
        this.notice_pdf = in.readString();
        this.notice_type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(noticeClass);
        dest.writeString(subject);
        dest.writeString(date);
        dest.writeString(notice);
        dest.writeString(notice_pdf);
        dest.writeString(notice_type);
    }
}
