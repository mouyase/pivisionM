package com.reiya.pixiv.concern;

import com.reiya.pixiv.base.BasePresenter;
import com.reiya.pixiv.base.BaseView;
import com.reiya.pixiv.bean.UserPreview;

import java.util.List;

/**
 * Created by lenovo on 2016/6/3.
 */

public interface ConcernContract {
    interface View extends BaseView {
        void showList(List<UserPreview> users, String nextUrl);
        void failToRemove();
        void removed(int id);
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void loadList(int page, String type);
        abstract void loadMore(int page, String url);
        abstract void remove(int page, int id);
    }
}
