package com.reiya.pixiv.spotlight;

import android.util.Log;

import com.reiya.pixiv.network.HttpService;
import com.reiya.pixiv.network.NetworkRequest;

import rx.Subscriber;

/**
 * Created by lenovo on 2016/6/3.
 */

public class SpotlightPresenter extends SpotlightContract.Presenter {

    @Override
    void loadList() {
        NetworkRequest.getSpotlight()
                .subscribe(new Subscriber<HttpService.SpotlightResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("err", e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(HttpService.SpotlightResponse spotlightResponse) {
                        getView().showList(spotlightResponse.getArticles(), spotlightResponse.getNextUrl());
                    }
                });
    }

    @Override
    void loadMore(String nextUrl) {
        NetworkRequest.getSpotlightNext(nextUrl)
                .subscribe(new Subscriber<HttpService.SpotlightResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpService.SpotlightResponse spotlightResponse) {
                        getView().showMore(spotlightResponse.getArticles(), spotlightResponse.getNextUrl());
                    }
                });
    }
}
