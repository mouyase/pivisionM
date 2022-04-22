package com.reiya.pixiv.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.reiya.pixiv.bean.Article;
import com.reiya.pixiv.bean.BookmarkDetail;
import com.reiya.pixiv.bean.BookmarkTag;
import com.reiya.pixiv.bean.Profile;
import com.reiya.pixiv.bean.TrendTag;
import com.reiya.pixiv.bean.User;
import com.reiya.pixiv.bean.UserPreview;
import com.reiya.pixiv.bean.Work;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by lenovo on 2016/4/4.
 */
public interface HttpService {
    @FormUrlEncoded
    @POST("auth/token")
    Observable<AuthResponse> getAuth(@Field("username") String userName,
                                     @Field("password") String password,
                                     @Field("grant_type") String gt,
                                     @Field("client_id") String ci,
                                     @Field("client_secret") String cs,
                                     @Field("device_token") String dt);

    @FormUrlEncoded
    @POST("auth/token")
    Observable<AuthResponse> getAuth(@Field("refresh_token") String refresh_token,
                                     @Field("grant_type") String gt,
                                     @Field("client_id") String ci,
                                     @Field("client_secret") String cs,
                                     @Field("device_token") String dt);

    @FormUrlEncoded
    @POST("auth/token")
    Observable<AuthResponse> getAuth(@Field("code") String code,
                                     @Field("code_verifier") String code_verifier,
                                     @Field("grant_type") String gt,
                                     @Field("client_id") String ci,
                                     @Field("client_secret") String cs,
                                     @Field("device_token") String dt,
                                     @Field("redirect_uri") String redirect_uri);

    @GET("v1/illust/ranking")
    Observable<IllustListResponse> getRanking(@Header("Authorization") String authorization, @Query("mode") String mode);

    @GET("v1/illust/ranking")
    Observable<IllustListResponse> getRanking(@Header("Authorization") String authorization, @Query("mode") String mode, @Query("date") String date);

    @GET
    Observable<IllustListResponse> getRankingNext(@Header("Authorization") String authorization, @Url String url);

    @GET("v1/user/bookmarks/illust")
    Observable<IllustListResponse> getBookmark(@Header("Authorization") String authorization,
                                               @Query("user_id") int userId,
                                               @Query("restrict") String restrict,
                                               @Query("tag") String tag);

    @GET
    Observable<IllustListResponse> getBookmarkNext(@Header("Authorization") String authorization, @Url String url);

    @GET("v1/user/bookmark-tags/illust")
    Observable<BookmarkTagsResponse> getBookmarkTags(@Header("Authorization") String authorization,
                                                     @Query("user_id") int userId,
                                                     @Query("restrict") String restrict);

    @GET
    Observable<BookmarkTagsResponse> getBookmarkTagsNext(@Header("Authorization") String authorization, @Url String url);

    @GET("v2/illust/bookmark/detail")
    Observable<BookmarkDetailResponse> getBookmarkDetail(@Header("Authorization") String authorization,
                                                         @Query("illust_id") String illust_id);

    @FormUrlEncoded
    @POST("v2/illust/bookmark/add")
    Observable<IllustListResponse> addBookmark(@Header("Authorization") String authorization,
                                               @Field("illust_id") String illust_id,
                                               @Field("restrict") String restrict,
                                               @Field("tags[]") String... tags);

    @FormUrlEncoded
    @POST("v1/illust/bookmark/delete")
    Observable<IllustListResponse> removeBookmark(@Header("Authorization") String authorization, @Field("illust_id") String illust_id);

    @GET("v1/user/following")
    Observable<FollowResponse> getFollow(@Header("Authorization") String authorization, @Query("user_id") int id, @Query("restrict") String restrict);

    @GET
    Observable<FollowResponse> getFollow(@Header("Authorization") String authorization, @Url String url);

    @FormUrlEncoded
    @POST("v1/user/follow/add")
    Observable<FollowResponse> addFollow(@Header("Authorization") String authorization, @Field("user_id") String id, @Field("restrict") String restrict);

    @FormUrlEncoded
    @POST("v1/user/follow/delete")
    Observable<FollowResponse> removeFollow(@Header("Authorization") String authorization, @Field("user_id") String id);

    @GET("v1/illust/comments")
    Observable<CommentResponse> getComment(@Header("Authorization") String authorization,
                                           @Query("illust_id") int illust_id);

    @GET
    Observable<CommentResponse> getComment(@Header("Authorization") String authorization,
                                           @Url String url);

    @GET("v1/search/illust")
    Observable<IllustListResponse> getFromPixiv(@Header("Authorization") String authorization,
                                                @Query("word") String word,
                                                @Query("content_type") String contentType,
                                                @Query("sort") String sort,
                                                @Query("search_target") String searchTarget);

    //高级搜索测试
    @GET("v1/search/illust")
    Observable<IllustListResponse> getFromPixiv(@Header("Authorization") String authorization,
                                                @Query("filter") String filter,
                                                @Query("include_translated_tag_results") boolean include_translated_tag_results,
                                                @Query("merge_plain_keyword_results") boolean merge_plain_keyword_results,
                                                @Query("word") String word,
                                                @Query("sort") String sort,
                                                @Query("search_target") String searchTarget,
                                                @Query("bookmark_num_min") String bookmark_min,
                                                @Query("bookmark_num_max") String bookmark_max);

    @GET
    Observable<IllustListResponse> getFromPixiv(@Header("Authorization") String authorization,
                                                @Url String url);

    @GET("v1/illust/ranking")
    Observable<IllustListResponse> getHistoryRanking(@Query("mode") String mode, @Query("date") String date);
//        Observable<IllustListResponse> getRanking(@Header("Authorization") String authorization, @Query("mode") String mode, @Query("date") String date);

    @GET
    Observable<IllustListResponse> getHistoryRanking(@Url String url);
    //        Observable<IllustListResponse> getRanking(@Header("Authorization") String authorization, @Url String url);

    @GET("v1/trending-tags/illust")
    Observable<TrendTagsResponse> getTrendTags(@Header("Authorization") String authorization);

    @GET("v1/user/detail")
    Observable<UserResponse> getUser(@Header("Authorization") String authorization, @Query("user_id") int id);

    @GET("v1/user/illusts")
//        Observable<IllustListResponse> getWorks(@Query("user_id") int id, @Query("type") String type);
    Observable<IllustListResponse> getWorksOfAUser(@Header("Authorization") String authorization, @Query("user_id") int id);

    @GET
    Observable<IllustListResponse> getWorksOfAUser(@Header("Authorization") String authorization, @Url String url);

    @GET("v2/illust/follow")
    Observable<IllustListResponse> getNewWorks(@Header("Authorization") String authorization, @Query("restrict") String restrict, @Query("content_type") String contentType);

    @GET
    Observable<IllustListResponse> getNewWorks(@Header("Authorization") String authorization, @Url String url);

    @GET("v1/illust/detail")
    Observable<WorkResponse> getWorkDetail(@Header("Authorization") String authorization, @Query("illust_id") int illust_id);

    @GET("v1/illust/detail")
    Observable<WorkResponse> getWorkDetail(@Query("illust_id") int illust_id);

    @GET("v1/spotlight/articles")
    Observable<SpotlightResponse> getSpotlight(@Header("Authorization") String authorization, @Header("Accept-Language") String language);

    @GET
    Observable<SpotlightResponse> getSpotlight(@Header("Authorization") String authorization, @Header("Accept-Language") String language, @Url String url);

    @GET
    Observable<ResponseBody> download(@Url String fileUrl, @Header("Referer") String referer);

    @GET("convert")
    Observable<UgoiraResponse> getUgoira(@Query("url") String url, @Query("format") String format);

    @GET("/v2/illust/related")
    Observable<IllustListResponse> getRelatedWorks(@Header("Authorization") String authorization,
                                                   @Query("illust_id") int id);

    @GET("v1/illust/recommended-nologin")
    Observable<RecommendResponse> getRecommendedWorks(@Query("filter") String filter,
                                                      @Query("include_ranking_illusts") boolean includeRanking);

    @GET
    Observable<RecommendResponse> getRecommendedWorks(@Url String url);

    @GET("v1/illust/recommended")
    Observable<RecommendResponse> getRecommendedWorks(@Header("Authorization") String authorization,
                                                      @Query("filter") String filter,
                                                      @Query("include_ranking_illusts") boolean includeRanking);

    @GET
    Observable<RecommendResponse> getRecommendedWorks(@Header("Authorization") String authorization,
                                                      @Url String url);

    class AuthResponse {
        @SerializedName("response")
        @Expose
        Response mResponse;

        public String getToken() {
            return mResponse.mToken;
        }

        public User getUser() {
            return mResponse.mUser;
        }

        public String getRefreshToken() {
            return mResponse.mRefreshToken;
        }

        static class Response {
            @SerializedName("access_token")
            @Expose
            String mToken;
            @SerializedName("user")
            @Expose
            User mUser;
            @SerializedName("refresh_token")
            @Expose
            String mRefreshToken;
        }
    }

    class SignResponse {

    }

    class IllustListResponse {
        @SerializedName("illusts")
        @Expose
        List<Work> mWorks;
        @SerializedName("next_url")
        @Expose
        String mNextUrl;

        public List<Work> getWorks() {
            return mWorks;
        }

        public String getNextUrl() {
            return mNextUrl;
        }
    }

    class BookmarkTagsResponse {
        @SerializedName("bookmark_tags")
        @Expose
        List<BookmarkTag> mBookmarkTags;
        @SerializedName("next_url")
        @Expose
        String mNextUrl;

        public List<BookmarkTag> getBookmarkTags() {
            return mBookmarkTags;
        }

        public String getNextUrl() {
            return mNextUrl;
        }
    }

    class BookmarkDetailResponse {
        @SerializedName("bookmark_detail")
        BookmarkDetail mBookmarkDetail;

        public BookmarkDetail getBookmarkDetail() {
            return mBookmarkDetail;
        }
    }

    class FollowResponse {
        @SerializedName("user_previews")
        @Expose
        List<UserPreview> mUserPreviews;
        @SerializedName("next_url")
        @Expose
        String mNextUrl;

        public List<UserPreview> getUserPreviews() {
            return mUserPreviews;
        }

        public String getNextUrl() {
            return mNextUrl;
        }
    }

    class CommentResponse {
        @SerializedName("comments")
        @Expose
        List<com.reiya.pixiv.bean.Comment> mComments;
        @SerializedName("next_url")
        @Expose
        String mNextUrl;

        public List<com.reiya.pixiv.bean.Comment> getComments() {
            return mComments;
        }

        public String getNextUrl() {
            return mNextUrl;
        }
    }

    class TrendTagsResponse {
        @SerializedName("trend_tags")
        @Expose
        List<TrendTag> mTrendTags;

        public List<TrendTag> getTrendTags() {
            return mTrendTags;
        }
    }

    class UserResponse {
        @SerializedName("user")
        @Expose
        User mUser;
        @SerializedName("profile")
        @Expose
        Profile mProfile;

        public User getUser() {
            return mUser;
        }

        public Profile getProfile() {
            return mProfile;
        }
    }

    class WorkResponse {
        @SerializedName("illust")
        @Expose
        Work mWork;

        public Work getWork() {
            return mWork;
        }
    }

    class SpotlightResponse {
        @SerializedName("spotlight_articles")
        @Expose
        List<Article> mArticles;
        @SerializedName("next_url")
        @Expose
        String mNextUrl;

        public List<Article> getArticles() {
            return mArticles;
        }

        public String getNextUrl() {
            return mNextUrl;
        }
    }

    class UgoiraResponse {
        @SerializedName("url")
        String mUrl;

        public String getUrl() {
            return mUrl;
        }
    }

    class SimpleResponse {

    }

    class RecommendResponse {
        @SerializedName("illusts")
        @Expose
        List<Work> mWorks;
        @SerializedName("next_url")
        @Expose
        String mNextUrl;
        @SerializedName("ranking_illusts")
        @Expose
        List<Work> mRankingWorks;

        public List<Work> getWorks() {
            return mWorks;
        }

        public String getNextUrl() {
            return mNextUrl;
        }

        public List<Work> getRankingWorks() {
            return mRankingWorks;
        }
    }
}
