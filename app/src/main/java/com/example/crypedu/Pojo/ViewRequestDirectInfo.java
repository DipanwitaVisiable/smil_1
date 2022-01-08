package com.example.crypedu.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hp on 6/21/2017.
 */
public class ViewRequestDirectInfo implements Parcelable {
    public String name;
    public String subjectStr;
    public String messageStr;
    public String dateStr;
    public String typeStr;
    public String classStr;
    public String sectionStr;

    public ViewRequestDirectInfo(String name, String subjectStr, String messageStr, String dateStr,
                                 String typeStr, String classStr, String sectionStr){
        this.name = name;
        this.subjectStr = subjectStr;
        this.messageStr = messageStr;
        this.dateStr = dateStr;
        this.classStr = classStr;
        this.typeStr = typeStr;
        this.sectionStr = sectionStr;
    }
    protected ViewRequestDirectInfo(Parcel in) {
        this.name = in.readString();
        this.subjectStr = in.readString();
        this.messageStr = in.readString();
        this.dateStr = in.readString();
        this.classStr = in.readString();
        this.typeStr = in.readString();
        this.sectionStr = in.readString();
    }

    public static final Creator<ViewRequestDirectInfo> CREATOR = new Creator<ViewRequestDirectInfo>() {
        @Override
        public ViewRequestDirectInfo createFromParcel(Parcel in) {
            return new ViewRequestDirectInfo(in);
        }

        @Override
        public ViewRequestDirectInfo[] newArray(int size) {
            return new ViewRequestDirectInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(subjectStr);
        dest.writeString(messageStr);
        dest.writeString(dateStr);
        dest.writeString(classStr);
        dest.writeString(typeStr);
        dest.writeString(sectionStr);
    }
}
