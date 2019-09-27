package com.reiya.pixiv.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lenovo on 2016/4/5.
 */

public class ProfileImageUrls implements Parcelable {

    public static final Creator<ProfileImageUrls> CREATOR = new Creator<ProfileImageUrls>() {
        @Override
        public ProfileImageUrls createFromParcel(Parcel in) {
            return new ProfileImageUrls(in);
        }

        @Override
        public ProfileImageUrls[] newArray(int size) {
            return new ProfileImageUrls[size];
        }
    };
    @SerializedName("px_16x16")
    @Expose
    String px16x16;
    @SerializedName("px_50x50")
    @Expose
    String px50x50;
    @SerializedName("px_170x170")
    @Expose
    String px170x170;
    @SerializedName("medium")
    @Expose
    String medium;

    public ProfileImageUrls() {
    }

    protected ProfileImageUrls(Parcel in) {
        px16x16 = in.readString();
        px50x50 = in.readString();
        px170x170 = in.readString();
        medium = in.readString();
    }

    /**
     * @return The px16x16
     */
    public String getPx16x16() {
        return px16x16;
    }

    /**
     * @return The px50x50
     */
    public String getPx50x50() {
        return px50x50;
    }

    /**
     * @return The px170x170
     */
    public String getPx170x170() {
        return px170x170;
    }

    public String getMedium() {
        return medium;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(px16x16);
        dest.writeString(px50x50);
        dest.writeString(px170x170);
        dest.writeString(medium);
    }
}