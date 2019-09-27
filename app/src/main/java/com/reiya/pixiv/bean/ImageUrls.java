package com.reiya.pixiv.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lenovo on 2016/4/3.
 */
public class ImageUrls implements Parcelable {
    public static final Creator<ImageUrls> CREATOR = new Creator<ImageUrls>() {
        @Override
        public ImageUrls createFromParcel(Parcel in) {
            return new ImageUrls(in);
        }

        @Override
        public ImageUrls[] newArray(int size) {
            return new ImageUrls[size];
        }
    };
    @SerializedName("square_medium")
    @Expose
    String squareMedium;
    @SerializedName("medium")
    @Expose
    String medium;
    @SerializedName("large")
    @Expose
    String large;
    @SerializedName("original")
    @Expose
    String original;

    public ImageUrls() {
    }

    protected ImageUrls(Parcel in) {
        squareMedium = in.readString();
        medium = in.readString();
        large = in.readString();
        original = in.readString();
    }

    /**
     * @return The squareMedium
     */
    public String getSquareMedium() {
        return squareMedium;
    }

    /**
     * @param squareMedium The square_medium
     */
    public void setSquareMedium(String squareMedium) {
        this.squareMedium = squareMedium;
    }

    /**
     * @return The medium
     */
    public String getMedium() {
        return medium;
    }

    /**
     * @param medium The medium
     */
    public void setMedium(String medium) {
        this.medium = medium;
    }

    /**
     * @return The large
     */
    public String getLarge() {
        return large;
    }

    /**
     * @param large The large
     */
    public void setLarge(String large) {
        this.large = large;
    }

    /**
     * @return The original
     */
    public String getOriginal() {
        return original;
    }

    /**
     * @param original The original
     */
    public void setOriginal(String original) {
        this.original = original;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(squareMedium);
        dest.writeString(medium);
        dest.writeString(large);
        dest.writeString(original);
    }
}
