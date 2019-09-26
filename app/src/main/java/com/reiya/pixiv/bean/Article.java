package com.reiya.pixiv.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/1/12 0012.
 */
public class Article {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("article_url")
    @Expose
    private String articleUrl;
    @SerializedName("publish_date")
    @Expose
    private String publishDate;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public String getPublishDate() {
        return publishDate;
    }

}
