package com.reiya.pixiv.collection;

import com.reiya.pixiv.base.BasePresenter;
import com.reiya.pixiv.base.BaseView;
import com.reiya.pixiv.bean.Work;

import java.util.List;

/**
 * Created by lenovo on 2016/6/3.
 */

interface CollectionContract {
    interface View extends BaseView {
        void showList(List<Work> works, String nextUrl);
        void failToRemove();
        void removed(int id);
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void loadList(int page, String type, String tag);
        abstract void loadMore(int page, String url);
        abstract void remove(int page, int id);
    }
}
