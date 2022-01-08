package com.example.crypedu.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sudipta on 08-12-2017.
 */

public class AccountInfo implements Parcelable {
    public String subject;
    public String date;
    public String account;
    public String postBy;


    public AccountInfo(String subject, String account, String date, String postBy){
        this.subject = subject;
        this.date = date;
        this.account = account;
        this.postBy = postBy;
    }

    private AccountInfo(Parcel in){
        this.subject = in.readString();
        this.date = in.readString();
        this.account = in.readString();
        this.postBy = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(subject);
        dest.writeString(account);
        dest.writeString(date);
        dest.writeString(postBy);
    }

    public static final Creator<AccountInfo> CREATOR = new Creator<AccountInfo>() {
        public AccountInfo createFromParcel(Parcel in) {
            return new AccountInfo(in);
        }

        public AccountInfo[] newArray(int size) {
            return new AccountInfo[size];

        }
    };
}