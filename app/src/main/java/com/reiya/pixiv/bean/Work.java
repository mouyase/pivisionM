package com.reiya.pixiv.bean;

/**
 * Created by lenovo on 2016/4/3.
 */


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.reiya.pixiv.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

public class Work implements Parcelable {

    @SerializedName("id")
    @Expose
     int id;
    @SerializedName("title")
    @Expose
     String title;
    @SerializedName("type")
    @Expose
     String type;
    @SerializedName("image_urls")
    @Expose
     ImageUrls imageUrls;
    @SerializedName("caption")
    @Expose
     String caption;
    @SerializedName("restrict")
    @Expose
     int restrict;
    @SerializedName("user")
    @Expose
     User user;
    @SerializedName("tags")
    @Expose
     List<Tag> tags = new ArrayList<>();
    @SerializedName("tools")
    @Expose
     List<String> tools = new ArrayList<>();
    @SerializedName("create_date")
    @Expose
     String createDate;
    @SerializedName("page_count")
    @Expose
     int pageCount;
    @SerializedName("width")
    @Expose
     int width;
    @SerializedName("height")
    @Expose
     int height;
    @SerializedName("sanity_level")
    @Expose
     int sanityLevel;
    @SerializedName("meta_single_page")
    @Expose
     MetaSinglePage metaSinglePage;
    @SerializedName("meta_pages")
    @Expose
     List<MetaPage> metaPages = new ArrayList<>();
    @SerializedName("total_view")
    @Expose
     int totalView;
    @SerializedName("total_bookmarks")
    @Expose
     int totalBookmarks;
    @SerializedName("is_bookmarked")
    @Expose
     boolean isBookmarked;
    @SerializedName("visible")
    @Expose
     boolean visible;

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
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    public boolean isDynamic() {
        return type.equals("ugoira");
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The imageUrls
     */
    public ImageUrls getImageUrls() {
        return imageUrls;
    }

    public String getImageUrl(int level) {
        switch (level) {
            case 0:
                return imageUrls.getSquareMedium();
            case 1:
                return imageUrls.getMedium();
            case 2:
                return imageUrls.getLarge();
        }
        return metaSinglePage.getOriginalImageUrl();
    }

    public String getImageUrl(int level, int index) {
        switch (level) {
            case 0:
                return metaPages.get(index).getImageUrls().getSquareMedium();
            case 1:
                return metaPages.get(index).getImageUrls().getMedium();
            case 2:
                return metaPages.get(index).getImageUrls().getLarge();
        }
        return metaPages.get(index).getImageUrls().getOriginal();
    }

    /**
     *
     * @param imageUrls
     * The image_urls
     */
    public void setImageUrls(ImageUrls imageUrls) {
        this.imageUrls = imageUrls;
    }

    /**
     *
     * @return
     * The caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     *
     * @param caption
     * The caption
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     *
     * @return
     * The restrict
     */
    public int getRestrict() {
        return restrict;
    }

    /**
     *
     * @param restrict
     * The restrict
     */
    public void setRestrict(int restrict) {
        this.restrict = restrict;
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

    /**
     *
     * @return
     * The tags
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     *
     * @param tags
     * The tags
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     *
     * @return
     * The tools
     */
    public List<String> getTools() {
        return tools;
    }

    /**
     *
     * @param tools
     * The tools
     */
    public void setTools(List<String> tools) {
        this.tools = tools;
    }

    /**
     *
     * @return
     * The createDate
     */
    public String getCreateDate() {
        return createDate;
    }

    public String getTime() {
        return StringHelper.getFormattedTime(createDate);
    }

    /**
     *
     * @param createDate
     * The create_date
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     *
     * @return
     * The pageCount
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     *
     * @param pageCount
     * The page_count
     */
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    /**
     *
     * @return
     * The width
     */
    public int getWidth() {
        return width;
    }

    /**
     *
     * @param width
     * The width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     *
     * @return
     * The height
     */
    public int getHeight() {
        return height;
    }

    /**
     *
     * @param height
     * The height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     *
     * @return
     * The sanityLevel
     */
    public int getSanityLevel() {
        return sanityLevel;
    }

    /**
     *
     * @param sanityLevel
     * The sanity_level
     */
    public void setSanityLevel(int sanityLevel) {
        this.sanityLevel = sanityLevel;
    }

    /**
     *
     * @return
     * The metaSinglePage
     */
    public MetaSinglePage getMetaSinglePage() {
        return metaSinglePage;
    }

    /**
     *
     * @param metaSinglePage
     * The meta_single_page
     */
    public void setMetaSinglePage(MetaSinglePage metaSinglePage) {
        this.metaSinglePage = metaSinglePage;
    }

    /**
     *
     * @return
     * The metaPages
     */
    public List<MetaPage> getMetaPages() {
        return metaPages;
    }

    /**
     *
     * @param metaPages
     * The meta_pages
     */
    public void setMetaPages(List<MetaPage> metaPages) {
        this.metaPages = metaPages;
    }

    /**
     *
     * @return
     * The totalView
     */
    public int getTotalView() {
        return totalView;
    }

    /**
     *
     * @param totalView
     * The total_view
     */
    public void setTotalView(int totalView) {
        this.totalView = totalView;
    }

    /**
     *
     * @return
     * The totalBookmarks
     */
    public int getTotalBookmarks() {
        return totalBookmarks;
    }

    /**
     *
     * @param totalBookmarks
     * The total_bookmarks
     */
    public void setTotalBookmarks(int totalBookmarks) {
        this.totalBookmarks = totalBookmarks;
    }

    /**
     *
     * @return
     * The isBookmarked
     */
    public boolean isIsBookmarked() {
        return isBookmarked;
    }

    /**
     *
     * @param isBookmarked
     * The is_bookmarked
     */
    public void setIsBookmarked(boolean isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    /**
     *
     * @return
     * The visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     *
     * @param visible
     * The visible
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.type);
        dest.writeParcelable(this.imageUrls, flags);
        dest.writeString(this.caption);
        dest.writeInt(this.restrict);
        dest.writeParcelable(this.user, flags);
        dest.writeTypedList(tags);
        dest.writeStringList(this.tools);
        dest.writeString(this.createDate);
        dest.writeInt(this.pageCount);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeInt(this.sanityLevel);
        dest.writeParcelable(this.metaSinglePage, flags);
        dest.writeTypedList(metaPages);
        dest.writeInt(this.totalView);
        dest.writeInt(this.totalBookmarks);
        dest.writeByte(isBookmarked ? (byte) 1 : (byte) 0);
        dest.writeByte(visible ? (byte) 1 : (byte) 0);
    }

    protected Work(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.type = in.readString();
        this.imageUrls = in.readParcelable(ImageUrls.class.getClassLoader());
        this.caption = in.readString();
        this.restrict = in.readInt();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.tags = in.createTypedArrayList(Tag.CREATOR);
        this.tools = in.createStringArrayList();
        this.createDate = in.readString();
        this.pageCount = in.readInt();
        this.width = in.readInt();
        this.height = in.readInt();
        this.sanityLevel = in.readInt();
        this.metaSinglePage = in.readParcelable(MetaSinglePage.class.getClassLoader());
        this.metaPages = in.createTypedArrayList(MetaPage.CREATOR);
        this.totalView = in.readInt();
        this.totalBookmarks = in.readInt();
        this.isBookmarked = in.readByte() != 0;
        this.visible = in.readByte() != 0;
    }

    public static final Creator<Work> CREATOR = new Creator<Work>() {
        @Override
        public Work createFromParcel(Parcel source) {
            return new Work(source);
        }

        @Override
        public Work[] newArray(int size) {
            return new Work[size];
        }
    };

    public Work() {
    }
}
