package com.reiya.pixiv.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lenovo on 2016/4/7.
 */
public class TrendTag {
    @SerializedName("tag")
    @Expose
    String mTag;
    @SerializedName("illust")
    @Expose
    Work mWork;

    public String getTag() {
        return mTag;
    }

    public Work getWork() {
        return mWork;
    }
}
