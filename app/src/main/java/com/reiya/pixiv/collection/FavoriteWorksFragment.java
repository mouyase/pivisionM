package com.reiya.pixiv.collection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.reiya.pixiv.adapter.BaseAdapter;
import com.reiya.pixiv.adapter.ImageAdapter;
import com.reiya.pixiv.base.BaseFragment;
import com.reiya.pixiv.bean.Work;
import com.reiya.pixiv.dialog.BookmarkTagFilterDialog;
import com.reiya.pixiv.util.Value;
import com.reiya.pixiv.view.LoaderRecyclerView;
import com.reiya.pixiv.view.WorkGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import tech.yojigen.pivisionm.R;

/**
 * Created by Administrator on 2015/12/16 0016.
 */
public class FavoriteWorksFragment extends BaseFragment<CollectionPresenter> implements CollectionContract.View, BookmarkTagFilterDialog.OnBookmarkTagSelectedCallback {
    public static final String[] TYPE = new String[]{Value.PUBLIC, Value.PRIVATE};
    private int mPage;
    private ImageAdapter mAdapter;
    private CollectionPresenter mPresenter;
    private LoaderRecyclerView mRecyclerView;
    private boolean mIsVisible = false;
    private String mType;

    public static FavoriteWorksFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(PAGE, page);
        FavoriteWorksFragment fragment = new FavoriteWorksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPresenter = new CollectionPresenter();
        mPresenter.setView(this);
        mPage = getArguments().getInt(PAGE);
        mType = TYPE[mPage];
        mRecyclerView = (LoaderRecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ImageAdapter(getActivity(), new ArrayList<Work>());
//        mAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int id, List<Work> list, int position) {
//                Intent intent = new Intent(getActivity(), ViewActivity.class);
//                intent.putExtra("id", id);
//                intent.putExtra("works", (ArrayList) list);
//                intent.putExtra("position", position);
//                startActivity(intent);
//            }
//        });
        mAdapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(Object item, View view) {
                final Work work = (Work) item;
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.getMenu().add(R.string.remove_collection);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        mPresenter.remove(mPage, work.getId());

                        return false;
                    }
                });
                popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu menu) {

                    }
                });
                popupMenu.show();
            }
        });

        mRecyclerView.setLayoutManager(new WorkGridLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnLoadMoreListener(new LoaderRecyclerView.OnLoadMoreListener() {
            @Override
            public void load() {
                if (mAdapter.getNextUrl() != null) {
                    mPresenter.loadMore(mPage, mAdapter.getNextUrl());
                }
            }
        });
        getList(null);
        return mRecyclerView;
    }

    private void getList(String tag) {
        mPresenter.loadList(mPage, mType, tag);

//        HttpClient.requestWithBearer(String.format(url, 1), new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                Log.e("ERR", e.getLocalizedMessage());
//                if (getActivity() == null) {
//                    return;
//                }
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getActivity().getApplicationContext(), R.string.fail_to_load, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                if (getActivity() == null) {
//                    return;
//                }
//                ResponseBody responseBody = response.body();
//                final String s = responseBody.string();
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mAdapter.setItems(JsonReader.getFavoriteWorks(s));
//                    }
//                });
//            }
//        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisible = isVisibleToUser;
        if (getActivity() != null) {
            if (isVisibleToUser) {
                enter();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsVisible) {
            enter();
        }
    }

    private void enter() {
        if (getActivity() != null) {
            ((FavoriteWorksActivity) getActivity()).setCurrentPart(mType, this);
        }
    }

    @Override
    public void showList(List<Work> works, String nextUrl) {
        mAdapter.addItems(works);
        mAdapter.setNextUrl(nextUrl);
        mRecyclerView.finishLoading();
    }

    @Override
    public void failToRemove() {
        Toast.makeText(getActivity().getApplicationContext(), R.string.fail_to_remove_collection, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removed(int id) {
        mAdapter.removeItem(id);
        Toast.makeText(getActivity().getApplicationContext(), R.string.removed_collection, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTagSelected(String tag) {
        mAdapter.clearItems();
        getList(tag);
    }
}
