package com.reiya.pixiv.spotlight;

import com.reiya.pixiv.base.BasePresenter;
import com.reiya.pixiv.base.BaseView;
import com.reiya.pixiv.bean.Article;

import java.util.List;

/**
 * Created by lenovo on 2016/6/3.
 */

interface SpotlightContract {
    interface View extends BaseView {
        void showList(List<Article> articles, String nextUrl);

        void showMore(List<Article> articles, String nextUrl);
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void loadList();

        abstract void loadMore(String nextUrl);
    }
}
