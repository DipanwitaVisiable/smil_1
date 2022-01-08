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

public class HomeWorkInfo implements Parcelable {
    public String subject;
    public String topic;
    private String home_class;
    public String assign;
    public String submit;

    public HomeWorkInfo(String subject, String topic, String home_class, String assign, String submit){
        this.subject = subject;
        this.topic = topic;
        this.home_class = home_class;
        this.assign = assign;
        this.submit = submit;
    }

    private HomeWorkInfo(Parcel in){
        this.subject = in.readString();
        this.topic = in.readString();
        this.home_class = in.readString();
        this.submit = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subject);
        dest.writeString(topic);
        dest.writeString(home_class);
        dest.writeString(assign);
        dest.writeString(submit);
    }

    public static final Creator<HomeWorkInfo> CREATOR = new Creator<HomeWorkInfo>() {
        public HomeWorkInfo createFromParcel(Parcel in) {
            return new HomeWorkInfo(in);
        }

        public HomeWorkInfo[] newArray(int size) {
            return new HomeWorkInfo[size];

        }
    };
}

