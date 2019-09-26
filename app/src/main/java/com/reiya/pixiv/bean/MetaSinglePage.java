
package com.reiya.pixiv.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetaSinglePage implements Parcelable {

    @SerializedName("original_image_url")
    @Expose
     String originalImageUrl;

    public MetaSinglePage() {
    }

    protected MetaSinglePage(Parcel in) {
        originalImageUrl = in.readString();
    }

    public static final Creator<MetaSinglePage> CREATOR = new Creator<MetaSinglePage>() {
        @Override
        public MetaSinglePage createFromParcel(Parcel in) {
            return new MetaSinglePage(in);
        }

        @Override
        public MetaSinglePage[] newArray(int size) {
            return new MetaSinglePage[size];
        }
    };

    public String getOriginalImageUrl() {
        return originalImageUrl;
    }

    public void setOriginalImageUrl(String originalImageUrl) {
        this.originalImageUrl = originalImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalImageUrl);
    }
}
