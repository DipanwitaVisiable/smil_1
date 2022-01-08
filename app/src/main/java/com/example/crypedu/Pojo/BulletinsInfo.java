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

public class BulletinsInfo implements Parcelable {
    public String subject;
    public String date;
    public String notice;
    public String postBy;
    public String bullet_pdf;
    public String bullet_type;

    public BulletinsInfo(String subject, String date, String notice, String postBy, String bullet_pdf, String bullet_type){
        this.subject = subject;
        this.date = date;
        this.notice = notice;
        this.postBy = postBy;
        this.bullet_pdf = bullet_pdf;
        this.bullet_type = bullet_type;
    }

    private BulletinsInfo(Parcel in){
        this.subject = in.readString();
        this.date = in.readString();
        this.notice = in.readString();
        this.postBy = in.readString();
        this.bullet_pdf = in.readString();
        this.bullet_type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subject);
        dest.writeString(date);
        dest.writeString(notice);
        dest.writeString(postBy);
        dest.writeString(bullet_pdf);
        dest.writeString(bullet_type);
    }

    public static final Creator<BulletinsInfo> CREATOR = new Creator<BulletinsInfo>() {
        public BulletinsInfo createFromParcel(Parcel in) {
            return new BulletinsInfo(in);
        }

        public BulletinsInfo[] newArray(int size) {
            return new BulletinsInfo[size];

        }
    };
}

