package com.reiya.pixiv.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhengyirui on 2017/9/7.
 */

public class BookmarkTag {
    @SerializedName("name")
    private String mName;
    @SerializedName("count")
    private int mCount;
    @SerializedName("is_registered")
    private boolean mIsRegistered;

    public String getName() {
        return mName;
    }

    public int getCount() {
        return mCount;
    }

    public void setRegistered(boolean registered) {
        mIsRegistered = registered;
    }

    public boolean isRegistered() {
        return mIsRegistered;
    }

    public BookmarkTag() {
    }

    public BookmarkTag(String name) {
        mName = name;
        mIsRegistered = true;
    }
}
