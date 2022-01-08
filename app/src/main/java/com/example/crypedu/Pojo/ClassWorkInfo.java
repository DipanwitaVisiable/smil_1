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

public class ClassWorkInfo implements Parcelable {

    public String subject;
    public String id;
    public String topic;
    public String class_name;
    public String date_of_class;

    public ClassWorkInfo (String id, String subject, String topic, String class_name, String date_of_class){
        this.id = id;
        this.subject = subject;
        this.topic = topic;
        this.class_name = class_name;
        this.date_of_class = date_of_class;
    }
    private ClassWorkInfo(Parcel in) {
        this.id = in.readString();
        this.subject = in.readString();
        this.topic = in.readString();
        this.class_name = in.readString();
        this.date_of_class = in.readString();
    }

    public static final Creator<ClassWorkInfo> CREATOR = new Creator<ClassWorkInfo>() {
        @Override
        public ClassWorkInfo createFromParcel(Parcel in) {
            return new ClassWorkInfo(in);
        }

        @Override
        public ClassWorkInfo[] newArray(int size) {
            return new ClassWorkInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(subject);
        dest.writeString(topic);
        dest.writeString(class_name);
        dest.writeString(date_of_class);
    }
}
