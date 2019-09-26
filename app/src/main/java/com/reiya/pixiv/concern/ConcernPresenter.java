package com.reiya.pixiv.concern;

import com.reiya.pixiv.network.HttpService;
import com.reiya.pixiv.network.NetworkRequest;

import rx.Subscriber;

/**
 * Created by lenovo on 2016/6/3.
 */

public class ConcernPresenter extends ConcernContract.Presenter {
    @Override
    void loadList(final int page, String type) {
        NetworkRequest.getFollow(type)
                .subscribe(new Subscriber<HttpService.FollowResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpService.FollowResponse followResponse) {
                        getView().showList(followResponse.getUserPreviews(), followResponse.getNextUrl());
                    }
                });
    }

    @Override
    void loadMore(final int page, String url) {
        NetworkRequest.getFollowNext(url)
                .subscribe(new Subscriber<HttpService.FollowResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpService.FollowResponse followResponse) {
                        getView().showList(followResponse.getUserPreviews(), followResponse.getNextUrl());
                    }
                });
    }

    @Override
    void remove(final int page, final int id) {
        NetworkRequest.removeFollow(id)
                .subscribe(new Subscriber<HttpService.FollowResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().failToRemove();
                    }

                    @Override
                    public void onNext(HttpService.FollowResponse followResponse) {
                        getView().removed(id);
                    }
                });
    }
}
