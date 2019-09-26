package com.reiya.pixiv.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhengyirui on 2017/9/7.
 */

public class BookmarkDetail {
    @SerializedName("tags")
    private List<BookmarkTag> mBookmarkTags;

    public List<BookmarkTag> getBookmarkTags() {
        return mBookmarkTags;
    }
}
