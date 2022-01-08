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

public class BannerInfo implements Parcelable {
    private String bannerId;
    public String bannerImage;

    public BannerInfo(String bannerId, String bannerImage){
        this.bannerId = bannerId;
        this.bannerImage = bannerImage;
    }

    private BannerInfo(Parcel in){
        this.bannerId = in.readString();
        this.bannerImage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bannerId);
        dest.writeString(bannerImage);
    }

    public static final Creator<BannerInfo> CREATOR = new Creator<BannerInfo>() {
        public BannerInfo createFromParcel(Parcel in) {
            return new BannerInfo(in);
        }

        public BannerInfo[] newArray(int size) {
            return new BannerInfo[size];

        }
    };
}

