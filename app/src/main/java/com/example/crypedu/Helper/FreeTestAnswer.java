package com.example.crypedu.Helper;

import android.os.Parcel;
import android.os.Parcelable;

public class FreeTestAnswer implements Parcelable {
    public String ans, ansId;

    public FreeTestAnswer(String ans, String ansId) {
        this.ans = ans;
        this.ansId = ansId;
    }

    protected FreeTestAnswer(Parcel in) {
        ans = in.readString();
        ansId = in.readString();
    }

    public static final Creator<FreeTestAnswer> CREATOR = new Creator<FreeTestAnswer>() {
        @Override
        public FreeTestAnswer createFromParcel(Parcel in) {
            return new FreeTestAnswer(in);
        }

        @Override
        public FreeTestAnswer[] newArray(int size) {
            return new FreeTestAnswer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ans);
        dest.writeString(ansId);
    }
}