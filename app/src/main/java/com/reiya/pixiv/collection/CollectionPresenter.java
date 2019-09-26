package com.reiya.pixiv.collection;

import com.reiya.pixiv.network.HttpService;
import com.reiya.pixiv.network.NetworkRequest;

import rx.Subscriber;

/**
 * Created by lenovo on 2016/6/3.
 */

class CollectionPresenter extends CollectionContract.Presenter {
    @Override
    void loadList(final int page, String type, String tag) {
        NetworkRequest.getBookmark(type, tag)
                .subscribe(new Subscriber<HttpService.IllustListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpService.IllustListResponse IllustListResponse) {
                        getView().showList(IllustListResponse.getWorks(), IllustListResponse.getNextUrl());
                    }
                });
    }

    @Override
    void loadMore(final int page, String url) {
        NetworkRequest.getBookmarkNext(url)
                .subscribe(new Subscriber<HttpService.IllustListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpService.IllustListResponse IllustListResponse) {
                        getView().showList(IllustListResponse.getWorks(), IllustListResponse.getNextUrl());
                    }
                });
    }

    @Override
    void remove(final int page, final int id) {
        NetworkRequest.removeBookmark(id)
                .subscribe(new Subscriber<HttpService.IllustListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().failToRemove();
                    }

                    @Override
                    public void onNext(HttpService.IllustListResponse IllustListResponse) {
                        getView().removed(id);
                    }
                });
    }
}
