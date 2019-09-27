package com.reiya.pixiv.base;

/**
 * Created by lenovo on 2016/5/31.
 */

public class BasePresenter<T extends BaseView> {
    private T mView;

    protected T getView() {
        return mView;
    }

    public void setView(T view) {
        mView = view;
    }

//    abstract void subscribe();
//    abstract void unsubscribe();
}
