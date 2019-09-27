package com.reiya.pixiv.bean;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import tech.yojigen.pivisionm.R;

/**
 * Created by lenovo on 2016/4/7.
 */
public class Profile {
    @SerializedName("webpage")
    @Expose
    private String webpage;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("birth")
    @Expose
    private String birth;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("job")
    @Expose
    private String job;
    @SerializedName("total_follow_users")
    @Expose
    private int totalFollowUsers;
    @SerializedName("total_follower")
    @Expose
    private int totalFollower;
    @SerializedName("total_mypixiv_users")
    @Expose
    private int totalMypixivUsers;
    @SerializedName("total_illusts")
    @Expose
    private int totalIllusts;
    @SerializedName("total_manga")
    @Expose
    private int totalManga;
    @SerializedName("total_novels")
    @Expose
    private int totalNovels;

    public String getInfo(Context context) {
        String info = "";
        if (!gender.equals("")) {
            if (gender.equals("male")) {
                info += context.getString(R.string.male) + " / ";
            } else {
                info += context.getString(R.string.female) + " / ";
            }
        }
        if (!region.equals("")) {
            info += region + " / ";
        }
        if (!birth.equals("")) {
            info += birth + " / ";
        }
        if (!job.equals("")) {
            info += job + " / ";
        }
        if (!info.equals("")) {
            info += "end";
            info = info.replace(" / end", "");
        }

        return info;
    }

    /**
     * @return The webpage
     */
    public String getWebpage() {
        return webpage;
    }

    /**
     * @return The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @return The birth
     */
    public String getBirth() {
        return birth;
    }

    /**
     * @return The region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @return The job
     */
    public String getJob() {
        return job;
    }

    /**
     * @return The totalFollowUsers
     */
    public int getTotalFollowUsers() {
        return totalFollowUsers;
    }

    /**
     * @return The totalFollower
     */
    public int getTotalFollower() {
        return totalFollower;
    }

    /**
     * @return The totalMypixivUsers
     */
    public int getTotalMypixivUsers() {
        return totalMypixivUsers;
    }

    /**
     * @return The totalIllusts
     */
    public int getTotalIllusts() {
        return totalIllusts;
    }

    /**
     * @return The totalManga
     */
    public int getTotalManga() {
        return totalManga;
    }

    /**
     * @return The totalNovels
     */
    public int getTotalNovels() {
        return totalNovels;
    }

}
