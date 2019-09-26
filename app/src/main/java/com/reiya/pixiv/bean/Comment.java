package com.reiya.pixiv.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.reiya.pixiv.util.StringHelper;

/**
 * Created by lenovo on 2016/2/17.
 */
public class Comment {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("user")
    @Expose
    private User user;

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The comment
     */
    public String getComment() {
        return comment;
    }

    /**
     *
     * @param comment
     * The comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     *
     * @return
     * The date
     */
    public String getDate() {
        return StringHelper.getFormattedTime(date);
    }

    /**
     *
     * @param date
     * The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return
     * The user
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @param user
     * The user
     */
    public void setUser(User user) {
        this.user = user;
    }
}
