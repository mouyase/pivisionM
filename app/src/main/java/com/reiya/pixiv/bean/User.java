package com.reiya.pixiv.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lenovo on 2016/4/3.
 */

public class User implements Parcelable {

    @SerializedName("id")
    @Expose
     int id;
    @SerializedName("name")
    @Expose
     String name;
    @SerializedName("account")
    @Expose
     String account;
    @SerializedName("profile_image_urls")
    @Expose
     ProfileImageUrls profileImageUrls;
    @SerializedName("comment")
    @Expose
     String comment;
    @SerializedName("is_followed")
    @Expose
     boolean isFollowed;

    public User() {
    }

    protected User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        account = in.readString();
        profileImageUrls = in.readParcelable(ProfileImageUrls.class.getClassLoader());
        isFollowed = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(account);
        dest.writeParcelable(profileImageUrls, flags);
        dest.writeByte((byte) (isFollowed ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /**
     *
     * @return
     *     The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     *     The account
     */
    public String getAccount() {
        return account;
    }

    /**
     *
     * @return
     *     The profileImageUrls
     */
    public ProfileImageUrls getProfileImageUrls() {
        return profileImageUrls;
    }

    public String getProfileImageUrl() {
        return profileImageUrls.getPx170x170();
    }

    public String getMediumImageUrl() {
        return profileImageUrls.getMedium();
    }

    public String getComment() {
        return comment;
    }

    /**
     *
     * @return
     *     The isFollowed
     */
    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }
}
