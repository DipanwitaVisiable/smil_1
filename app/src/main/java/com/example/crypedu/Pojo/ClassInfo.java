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

public class ClassInfo implements Parcelable {
    public String id;
    public String class_or_year;
    public String active;

    public ClassInfo(String id, String class_or_year, String active){
        this.id = id;
        this.class_or_year = class_or_year;
        this.active = active;
    }

    private ClassInfo(Parcel in){
        this.id = in.readString();
        this.class_or_year = in.readString();
        this.active = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(class_or_year);
        dest.writeString(active);
    }

    public static final Creator<ClassInfo> CREATOR = new Creator<ClassInfo>() {
        public ClassInfo createFromParcel(Parcel in) {
            return new ClassInfo(in);
        }

        public ClassInfo[] newArray(int size) {
            return new ClassInfo[size];

        }
    };
}
