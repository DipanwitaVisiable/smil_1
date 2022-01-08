package com.example.crypedu.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hp on 7/10/2017.
 */
public class ReplyCommunicationInfo implements Parcelable {

    public String replyMessage;
    public String requestBy;
    public String date;

    public ReplyCommunicationInfo(String replyMessage, String requestBy, String date){
        this.replyMessage = replyMessage;
        this.requestBy = requestBy;
        this.date = date;
    }
    private ReplyCommunicationInfo(Parcel in) {
        this.replyMessage = in.readString();
        this.requestBy = in.readString();
        this.date = in.readString();
    }

    public static final Creator<ReplyCommunicationInfo> CREATOR = new Creator<ReplyCommunicationInfo>() {
        @Override
        public ReplyCommunicationInfo createFromParcel(Parcel in) {
            return new ReplyCommunicationInfo(in);
        }

        @Override
        public ReplyCommunicationInfo[] newArray(int size) {
            return new ReplyCommunicationInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(replyMessage);
        dest.writeString(requestBy);
        dest.writeString(date);
    }
}
