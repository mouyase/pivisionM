package com.reiya.pixiv.profile;

import com.reiya.pixiv.base.BasePresenter;
import com.reiya.pixiv.base.BaseView;
import com.reiya.pixiv.bean.Profile;
import com.reiya.pixiv.bean.User;
import com.reiya.pixiv.bean.Work;

import java.util.List;

/**
 * Created by lenovo on 2016/6/3.
 */

interface ProfileContract {
    interface View extends BaseView {
        void showWorks(List<Work> works, String nextUrl);

        void showMore(List<Work> works, String nextUrl);

        void showUserInfo(User user, Profile profile);

        void failToLoad();
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void getWorks(int id);

        abstract void getMore(String url);

        abstract void getUser(int id);
    }
}
