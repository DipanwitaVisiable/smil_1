package com.example.crypedu.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hp on 6/21/2017.
 */
public class ViewRequestInfo implements Parcelable {
    public String commId;
    public String subjectStr;
    public String messageStr;
    public String fromDateStr;
    public String toDateStr;
    public String typeStr;
    public String statusStr;

    public ViewRequestInfo(String commId, String subjectStr, String messageStr, String fromDateStr,
                           String toDateStr, String typeStr, String statusStr){
        this.commId = commId;
        this.subjectStr = subjectStr;
        this.messageStr = messageStr;
        this.fromDateStr = fromDateStr;
        this.toDateStr = toDateStr;
        this.typeStr = typeStr;
        this.statusStr = statusStr;
    }
    protected ViewRequestInfo(Parcel in) {
        this.commId = in.readString();
        this.subjectStr = in.readString();
        this.messageStr = in.readString();
        this.fromDateStr = in.readString();
        this.toDateStr = in.readString();
        this.typeStr = in.readString();
        this.statusStr = in.readString();
    }

    public static final Creator<ViewRequestInfo> CREATOR = new Creator<ViewRequestInfo>() {
        @Override
        public ViewRequestInfo createFromParcel(Parcel in) {
            return new ViewRequestInfo(in);
        }

        @Override
        public ViewRequestInfo[] newArray(int size) {
            return new ViewRequestInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(commId);
        dest.writeString(subjectStr);
        dest.writeString(messageStr);
        dest.writeString(fromDateStr);
        dest.writeString(toDateStr);
        dest.writeString(typeStr);
        dest.writeString(statusStr);
    }
}
