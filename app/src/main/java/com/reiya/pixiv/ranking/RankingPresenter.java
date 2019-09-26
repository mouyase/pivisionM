package com.reiya.pixiv.ranking;

import com.reiya.pixiv.network.HttpService;
import com.reiya.pixiv.network.NetworkRequest;

import rx.Observable;
import rx.Subscriber;

import static com.reiya.pixiv.network.NetworkRequest.getRanking;

/**
 * Created by lenovo on 2016/5/31.
 */

class RankingPresenter extends RankingContract.Presenter {

    @Override
    void loadList(final int page, String type, int mode, String date) {
        getRanking(type, date)
                .subscribe(new Subscriber<HttpService.IllustListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().failToLoad();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(HttpService.IllustListResponse illustListResponse) {
                        getView().setList(illustListResponse.getWorks(), illustListResponse.getNextUrl());
                    }
                });
    }

    @Override
    void loadMore(final int page, String url) {
        Observable<HttpService.IllustListResponse> observable = NetworkRequest.getRankingNextWithBearer(url);
        observable.subscribe(new Subscriber<HttpService.IllustListResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpService.IllustListResponse illustListResponse) {
                getView().addList(illustListResponse.getWorks(), illustListResponse.getNextUrl());
            }
        });
    }
}
