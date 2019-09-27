package com.reiya.pixiv.main;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.reiya.pixiv.adapter.ImageAdapter;
import com.reiya.pixiv.network.HttpService;
import com.reiya.pixiv.network.NetworkRequest;
import com.reiya.pixiv.util.UserData;
import com.reiya.pixiv.view.LoaderRecyclerView;
import com.reiya.pixiv.view.WorkGridLayoutManager;

import rx.Subscriber;
import tech.yojigen.pivisionm.R;

/**
 * Created by lenovo on 2016/10/4.
 */

public class SubscribeFragment extends MainFragment {
    private ImageAdapter mAdapter;
    private String[] mBlacklist;
    private LoaderRecyclerView mRecyclerView;

    public static SubscribeFragment newInstance() {

        Bundle args = new Bundle();

        SubscribeFragment fragment = new SubscribeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlacklist = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.key_blacklist), "").split("\n");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (LoaderRecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        mAdapter = new ImageAdapter(getActivity());
        mAdapter.setBlackList(mBlacklist);
        mRecyclerView.setLayoutManager(new WorkGridLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnLoadMoreListener(new LoaderRecyclerView.OnLoadMoreListener() {
            @Override
            public void load() {
                if (mAdapter.getNextUrl() != null) {
                    NetworkRequest.getNewWorks(mAdapter.getNextUrl())
                            .subscribe(new Subscriber<HttpService.IllustListResponse>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(HttpService.IllustListResponse illustListResponse) {
                                    mAdapter.addItems(illustListResponse.getWorks());
                                    mAdapter.setNextUrl(illustListResponse.getNextUrl());
                                    mRecyclerView.finishLoading();
                                }
                            });
                }
            }
        });
        return mRecyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (UserData.isLoggedIn()) {
            getList();
        }
    }

    private void getList() {
        NetworkRequest.getNewWorks()
                .subscribe(new Subscriber<HttpService.IllustListResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getActivity() == null) {
                            return;
                        }
                        Snackbar.make(getView(), R.string.fail_to_load, Snackbar.LENGTH_LONG)
                                .setAction(R.string.retry, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getList();
                                    }
                                })
                                .show();
                    }

                    @Override
                    public void onNext(HttpService.IllustListResponse illustListResponse) {
                        mAdapter.addItems(illustListResponse.getWorks());
                        mAdapter.setNextUrl(illustListResponse.getNextUrl());
                    }
                });
    }

    @Override
    public void changeLayoutManager() {
        mRecyclerView.setLayoutManager(new WorkGridLayoutManager(getActivity()));
    }

    @Override
    public void changeListStyle() {
        mAdapter.setStyle();
    }
}
