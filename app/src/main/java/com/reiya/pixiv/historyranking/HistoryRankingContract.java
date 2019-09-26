package com.reiya.pixiv.historyranking;

import com.reiya.pixiv.base.BasePresenter;
import com.reiya.pixiv.base.BaseView;
import com.reiya.pixiv.bean.Work;

import java.util.List;

/**
 * Created by lenovo on 2016/6/3.
 */

interface HistoryRankingContract {
    interface View extends BaseView {
        void showList(List<Work> works, String nextUrl);
        void showMore(List<Work> works, String nextUrl);
        void failToLoad();
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void loadList(int page, String type, String date);
        abstract void loadMore(int page, String url);
    }
}
