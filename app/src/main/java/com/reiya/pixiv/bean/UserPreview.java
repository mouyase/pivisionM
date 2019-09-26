package com.reiya.pixiv.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lenovo on 2016/4/7.
 */
public class UserPreview {
    @SerializedName("user")
    @Expose
    User user;

    public User getUser() {
        return user;
    }
}
