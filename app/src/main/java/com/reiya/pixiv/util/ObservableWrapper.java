package com.reiya.pixiv.util;

import android.util.Log;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by lenovo on 2016/5/30.
 */

public class ObservableWrapper<T> {
    private Observable<T> mObservable;
    private Error defaultError = new Error() {
        @Override
        public void onDo() {

        }
    };

    public ObservableWrapper(Observable<T> observable) {
        mObservable = observable;
    }

    public void subscribe(final Action<T> action) {
        subscribe(action, defaultError);
    }

    public void subscribe(final Action<T> action, final Error error) {
        mObservable
                .subscribe(new Subscriber<T>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("error", e.getLocalizedMessage());
                        error.onDo();
                    }

                    @Override
                    public void onNext(T t) {
                        action.onDo(t);
                    }
                });
    }

    public interface Action<T> {
        void onDo(T t);
    }

    public interface Error {
        void onDo();
    }
}
