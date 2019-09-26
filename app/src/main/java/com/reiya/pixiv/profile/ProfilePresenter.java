package com.reiya.pixiv.profile;

import com.reiya.pixiv.network.HttpService;
import com.reiya.pixiv.network.NetworkRequest;

import rx.Subscriber;

/**
 * Created by lenovo on 2016/6/3.
 */

class ProfilePresenter extends ProfileContract.Presenter {
    @Override
    void getWorks(int id) {
        NetworkRequest.getWorksOfAUser(id)
                .subscribe(new Subscriber<HttpService.IllustListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getView().failToLoad();
                    }

                    @Override
                    public void onNext(HttpService.IllustListResponse illustListResponse) {
                        getView().showWorks(illustListResponse.getWorks(), illustListResponse.getNextUrl());
                    }
                });
    }

    @Override
    void getMore(String url) {
        NetworkRequest.getWorksOfAUserNext(url)
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

    @Override
    void getUser(int id) {
        NetworkRequest.getUserWithBearer(id)
                .subscribe(new Subscriber<HttpService.UserResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getView().failToLoad();
                    }

                    @Override
                    public void onNext(HttpService.UserResponse userResponse) {
                        getView().showUserInfo(userResponse.getUser(), userResponse.getProfile());
                    }
                });
    }
}
