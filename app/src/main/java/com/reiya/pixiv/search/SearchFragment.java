package com.reiya.pixiv.search;


import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import tech.yojigen.pivisionm.R;
import com.reiya.pixiv.adapter.ImageAdapter;
import com.reiya.pixiv.base.BaseFragment;
import com.reiya.pixiv.bean.Work;
import com.reiya.pixiv.view.LoaderRecyclerView;
import com.reiya.pixiv.view.WorkGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/9 0009.
 */
public class SearchFragment extends BaseFragment<SearchPresenter> implements SearchContract.View {
    private int mPage;
    private static final String KEYWORD = "keyword";
    private ImageAdapter adapter;
    private String keyword;
    private String[] blacklist;
    private SearchPresenter mPresenter;
    private LoaderRecyclerView mRecyclerView;
    private FrameLayout mLayout;

    public static SearchFragment newInstance(int page, String keyword) {
        Bundle args = new Bundle();
        args.putInt(PAGE, page);
        args.putString(KEYWORD, keyword);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(PAGE);
        keyword = getArguments().getString(KEYWORD);
        blacklist =  PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.key_blacklist), "").split("\n");
        mPresenter = new SearchPresenter();
        mPresenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = new FrameLayout(getActivity());
        mRecyclerView = (LoaderRecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        mRecyclerView.setLayoutManager(new WorkGridLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ImageAdapter(getActivity(), new ArrayList<Work>());
        adapter.setBlackList(blacklist);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setOnLoadMoreListener(new LoaderRecyclerView.OnLoadMoreListener() {
            @Override
            public void load() {
                if (adapter.getNextUrl() != null) {
                    mPresenter.next(adapter.getNextUrl());
                }
            }
        });
        mLayout.addView(mRecyclerView);
        return mLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.search(keyword, mPage);
    }

    @Override
    public void showList(List<Work> works, String nextUrl) {
        if (getActivity() == null) {
            return;
        }
        if (works.size() == 0) {
//            Toast.makeText(getActivity(), R.string.no_result_found, Toast.LENGTH_SHORT).show();
            TextView textView = (TextView) View.inflate(getActivity(), R.layout.textview_no_result_found, null);
            mLayout.addView(textView);
        } else {
            adapter.setItems(works);
            if (nextUrl != null) {
                adapter.setNextUrl(nextUrl);
            }
        }
    }

    @Override
    public void showMore(List<Work> works, String nextUrl) {
        if (getActivity() == null) {
            return;
        }
        adapter.addItems(works);
        adapter.setNextUrl(nextUrl);
        mRecyclerView.finishLoading();
    }

    @Override
    public void failToLoad() {
        if (getActivity() == null) {
            return;
        }
        Toast.makeText(getActivity(), R.string.fail_to_load, Toast.LENGTH_SHORT).show();
    }
}
