package com.reiya.pixiv.historyranking;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import tech.yojigen.pivisionm.R;
import com.reiya.pixiv.adapter.ImageAdapter;
import com.reiya.pixiv.base.BaseFragment;
import com.reiya.pixiv.bean.Work;
import com.reiya.pixiv.util.Value;
import com.reiya.pixiv.view.LoaderRecyclerView;
import com.reiya.pixiv.view.WorkGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/2/24.
 */
public class HistoryRankingFragment extends BaseFragment<HistoryRankingPresenter> implements HistoryRankingContract.View {
    private int mPage;
    private static final String DATE = "date";
    private String date;
    private ImageAdapter adapter;
    private String[] blacklist;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HistoryRankingPresenter mPresenter;
    private LoaderRecyclerView mRecyclerView;

    public static HistoryRankingFragment newInstance(int page, String date) {
        Bundle args = new Bundle();
        args.putInt(PAGE, page);
        args.putString(DATE, date);
        HistoryRankingFragment fragment = new HistoryRankingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = getArguments().getString(DATE);
        mPage = getArguments().getInt(PAGE);
        blacklist =  PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.key_blacklist), "").split("\n");
        mPresenter = new HistoryRankingPresenter();
        mPresenter.setView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String[] TYPE = {Value.DAILY, Value.WEEKLY, Value.MONTHLY};
        String type = TYPE[mPage];
        mRecyclerView = (LoaderRecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        mRecyclerView.setLayoutManager(new WorkGridLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ImageAdapter(getActivity(), new ArrayList<Work>());
        adapter.setBlackList(blacklist);
//        adapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int id, List<Work> works, int position) {
//                Intent intent = new Intent(getActivity(), ViewActivity.class);
//                intent.putExtra("id", id);
//                intent.putExtra("works", (ArrayList) works);
//                intent.putExtra("position", position);
//                startActivity(intent);
//            }
//        });
        mRecyclerView.setAdapter(adapter);
        final String finalType = type;
        mRecyclerView.setOnLoadMoreListener(new LoaderRecyclerView.OnLoadMoreListener() {
            @Override
            public void load() {
                mPresenter.loadMore(mPage, adapter.getNextUrl());
            }
        });

        swipeRefreshLayout = new SwipeRefreshLayout(getContext());
        swipeRefreshLayout.addView(mRecyclerView);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getList(finalType, swipeRefreshLayout);
            }
        });
        getList(type, swipeRefreshLayout);
        return swipeRefreshLayout;
    }

    private void getList(final String type, final SwipeRefreshLayout swipeRefreshLayout) {
        mPresenter.loadList(mPage, type, date);
//        HttpClient.requestWithBearer(String.format(url, type, date, 1), new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                ResponseBody responseBody = response.body();
//                final String s = responseBody.string();
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        swipeRefreshLayout.setRefreshing(false);
//                        List<SimpleItem> items = JsonReader.getList(s, blacklist);
//                        if (items.size() > 0) {
//                            adapter.setItems(items);
//                        }
//                    }
//                });
//            }
//        });
    }

    @Override
    public void showList(List<Work> works, String nextUrl) {
        swipeRefreshLayout.setRefreshing(false);
        adapter.setItems(works);
        adapter.setNextUrl(nextUrl);
    }

    @Override
    public void showMore(List<Work> works, String nextUrl) {
        adapter.addItems(works);
        adapter.setNextUrl(nextUrl);
        mRecyclerView.finishLoading();
    }

    @Override
    public void failToLoad() {
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getActivity(), R.string.fail_to_load, Toast.LENGTH_SHORT).show();
    }
}
