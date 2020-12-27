package com.reiya.pixiv.search;

import com.reiya.pixiv.network.HttpService;
import com.reiya.pixiv.network.NetworkRequest;

import rx.Subscriber;

/**
 * Created by lenovo on 2016/6/3.
 */

class SearchPresenter extends SearchContract.Presenter {
    private static final String[] SUFFIX = {" 10000users入り", " 5000users入り", " 1000users入り", ""};
    private static final String[] BOOKMARK_MIN = {"10000", "5000", "1000", "0"};
    private static final String[] BOOKMARK_MAX = {"99999", "9999", "4999", "99999"};

    @Override
    void search(String keyword, int index) {
        NetworkRequest.getSearchFromPixiv(keyword + SUFFIX[index])
//        NetworkRequest.getSearchFromPixivPlus(keyword, BOOKMARK_MIN[index], BOOKMARK_MAX[index])
                .subscribe(new Subscriber<HttpService.IllustListResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().failToLoad();
                    }

                    @Override
                    public void onNext(HttpService.IllustListResponse illustListResponse) {
                        getView().showList(illustListResponse.getWorks(), illustListResponse.getNextUrl());
                    }
                });
    }

    @Override
    void next(String url) {
        NetworkRequest.getSearchFromPixivNext(url)
                .subscribe(new Subscriber<HttpService.IllustListResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(HttpService.IllustListResponse illustListResponse) {
                        getView().showMore(illustListResponse.getWorks(), illustListResponse.getNextUrl());
                    }
                });
    }
}
