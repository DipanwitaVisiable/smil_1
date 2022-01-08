package com.example.crypedu.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 2/3/16.
 */
public class Mobile implements Parcelable{

    public String imageResource;
    public String modelName;
    public String price;

    public Mobile(String imageResource, String modelName, String price) {
        this.imageResource = imageResource;
        this.modelName = modelName;
        this.price = price;
    }

    protected Mobile(Parcel in) {
        imageResource = in.readString();
        modelName = in.readString();
        price = in.readString();
    }

    public static final Creator<Mobile> CREATOR = new Creator<Mobile>() {
        @Override
        public Mobile createFromParcel(Parcel in) {
            return new Mobile(in);
        }

        @Override
        public Mobile[] newArray(int size) {
            return new Mobile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageResource);
        dest.writeString(modelName);
        dest.writeString(price);
    }
}
