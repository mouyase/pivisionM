package com.reiya.pixiv.historyranking;

import com.reiya.pixiv.network.HttpService;
import com.reiya.pixiv.network.NetworkRequest;

import rx.Subscriber;

/**
 * Created by lenovo on 2016/6/3.
 */

class HistoryRankingPresenter extends HistoryRankingContract.Presenter {
    @Override
    void loadList(final int page, String type, String date) {
        NetworkRequest.getHistoryRanking(type, date)
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
    void loadMore(final int page, String url) {
        NetworkRequest.getHistoryRankingNext(url)
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
