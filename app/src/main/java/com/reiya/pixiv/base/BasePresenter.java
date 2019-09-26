package com.reiya.pixiv.base;

/**
 * Created by lenovo on 2016/5/31.
 */

public class BasePresenter<T extends BaseView> {
    private T mView;

    public void setView(T view) {
        mView = view;
    }

    protected T getView() {
        return mView;
    }

//    abstract void subscribe();
//    abstract void unsubscribe();
}
