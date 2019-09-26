package com.reiya.pixiv.search;

import com.reiya.pixiv.base.BasePresenter;
import com.reiya.pixiv.base.BaseView;
import com.reiya.pixiv.bean.Work;

import java.util.List;

/**
 * Created by lenovo on 2016/6/3.
 */

interface SearchContract {
    interface View extends BaseView {
        void showList(List<Work> works, String nextUrl);
        void showMore(List<Work> works, String nextUrl);
        void failToLoad();
    }

    abstract class Presenter extends BasePresenter<View> {
//        abstract void fromBmob(String keyword);
//        abstract void fromPixiv(String keyword);
//        abstract void fromPixivNext(String url);
        abstract void search(String keyword, int index);
        abstract void next(String url);
    }
}
