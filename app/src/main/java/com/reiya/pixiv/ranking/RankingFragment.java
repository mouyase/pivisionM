package com.reiya.pixiv.ranking;

import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tech.yojigen.pivisionm.R;
import com.reiya.pixiv.adapter.ImageAdapter;
import com.reiya.pixiv.base.BaseFragment;
import com.reiya.pixiv.bean.Work;
import com.reiya.pixiv.util.Value;
import com.reiya.pixiv.view.LoaderRecyclerView;
import com.reiya.pixiv.view.WorkGridLayoutManager;

import java.util.List;

/**
 * Created by Administrator on 2015/11/24 0024.
 */
public class RankingFragment extends BaseFragment<RankingPresenter> implements RankingContract.View {
    private int mPage;
    private static final String MODE = "mMode";
    private static final String DATE = "date";
    private ImageAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private int mMode = 0;
    private String mDate;
    private static final String[] TYPE = {Value.DAILY, Value.WEEKLY, Value.MONTHLY, Value.DAILY_R18, Value.WEEKLY_R18, Value.MALE_R18, Value.FEMALE_R18};
    private String[] blacklist;
    private RankingPresenter mPresenter;
    private LoaderRecyclerView mRecyclerView;

    public static RankingFragment newInstance(int page, int mode, String date) {
        Bundle args = new Bundle();
        args.putInt(PAGE, page);
        args.putInt(MODE, mode);
        args.putString(DATE, date);
        RankingFragment fragment = new RankingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(PAGE);
        mMode = getArguments().getInt(MODE);
        mDate = getArguments().getString(DATE);
        blacklist =  PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.key_blacklist), "").split("\n");
        mPresenter = new RankingPresenter();
        mPresenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final String type = TYPE[mPage];

        mRecyclerView = (LoaderRecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        layoutManager = new WorkGridLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ImageAdapter(getActivity());
        adapter.setBlackList(blacklist);
        mRecyclerView.setAdapter(adapter);

//        swipeRefreshLayout = new SwipeRefreshLayout(getContext());
//        swipeRefreshLayout.addView(mRecyclerView);
//        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getList(type);
//            }
//        });

        mRecyclerView.setOnLoadMoreListener(new LoaderRecyclerView.OnLoadMoreListener() {
            @Override
            public void load() {
                if (adapter.getNextUrl() != null) {
                    mPresenter.loadMore(mPage, adapter.getNextUrl());
                }
            }
        });

        getList(type);
        return mRecyclerView;
    }

    private void getList(final String type) {
        adapter.setLoading(true);
        mPresenter.loadList(mPage, type, mMode, mDate);
    }

    @Override
    public void setList(List<Work> works, String nextUrl) {
        adapter.setItems(works);
        adapter.setNextUrl(nextUrl);
    }

    @Override
    public void addList(List<Work> works, String nextUrl) {
        adapter.addItems(works);
        adapter.setNextUrl(nextUrl);
        mRecyclerView.finishLoading();
    }

    @Override
    public void failToLoad() {
        if (getActivity() == null) {
            return;
        }
        Snackbar.make(getView(), R.string.fail_to_load, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getList(TYPE[mPage]);
                    }
                })
                .show();
    }
}
