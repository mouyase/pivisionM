package com.reiya.pixiv.ranking;

import com.reiya.pixiv.base.BasePresenter;
import com.reiya.pixiv.base.BaseView;
import com.reiya.pixiv.bean.Work;

import java.util.List;

/**
 * Created by lenovo on 2016/5/31.
 */

interface RankingContract {

    interface View extends BaseView {

        void setList(List<Work> works, String nextUrl);

        void addList(List<Work> works, String nextUrl);

        void failToLoad();
    }

    abstract class Presenter extends BasePresenter<View> {

        abstract void loadList(int page, String type, int mode, String date);

        abstract void loadMore(int page, String url);
    }
}
