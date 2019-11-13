package com.reiya.pixiv.util;


import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lenovo on 2016/5/24.
 */

public class RxHelper {
    public static <T> Observable.Transformer<T, T> getSchedulerHelper() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
