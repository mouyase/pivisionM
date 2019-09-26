
package com.reiya.pixiv.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetaPage implements Parcelable {

    @SerializedName("image_urls")
    @Expose
     ImageUrls imageUrls;

    public MetaPage() {
    }

    protected MetaPage(Parcel in) {
        imageUrls = in.readParcelable(ImageUrls.class.getClassLoader());
    }

    public static final Creator<MetaPage> CREATOR = new Creator<MetaPage>() {
        @Override
        public MetaPage createFromParcel(Parcel in) {
            return new MetaPage(in);
        }

        @Override
        public MetaPage[] newArray(int size) {
            return new MetaPage[size];
        }
    };

    /**
     * 
     * @return
     *     The imageUrls
     */
    public ImageUrls getImageUrls() {
        return imageUrls;
    }

    /**
     * 
     * @param imageUrls
     *     The image_urls
     */
    public void setImageUrls(ImageUrls imageUrls) {
        this.imageUrls = imageUrls;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(imageUrls, flags);
    }
}
