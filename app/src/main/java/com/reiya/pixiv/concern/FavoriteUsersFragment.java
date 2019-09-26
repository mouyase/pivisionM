package com.reiya.pixiv.concern;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import tech.yojigen.pivisionm.R;
import com.reiya.pixiv.adapter.BaseAdapter;
import com.reiya.pixiv.adapter.UserAdapter;
import com.reiya.pixiv.base.BaseFragment;
import com.reiya.pixiv.bean.UserPreview;
import com.reiya.pixiv.util.Value;
import com.reiya.pixiv.view.LoaderRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/16 0016.
 */
public class FavoriteUsersFragment extends BaseFragment<ConcernPresenter> implements ConcernContract.View {
    private int mPage;
    private UserAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ConcernPresenter mPresenter;
    private LoaderRecyclerView mRecyclerView;

    public static FavoriteUsersFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(PAGE, page);
        FavoriteUsersFragment fragment = new FavoriteUsersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPresenter = new ConcernPresenter();
        mPresenter.setView(this);
        mPage = getArguments().getInt(PAGE);
        String[] TYPE = {Value.PUBLIC, Value.PRIVATE};
        String type = TYPE[mPage];
        mRecyclerView = (LoaderRecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new UserAdapter(getActivity(), new ArrayList<UserPreview>());
        adapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener() {
                                               @Override
                                               public void onLongClick(final Object item, View view) {
                                                   PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                                                   popupMenu.getMenu().add(R.string.remove_concern);
                                                   popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                       @Override
                                                       public boolean onMenuItemClick(MenuItem menuItem) {

                                                           mPresenter.remove(mPage, ((UserPreview) item).getUser().getId());

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

        mRecyclerView.setAdapter(adapter);
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

    private void getList(String type) {
        mPresenter.loadList(mPage, type);
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
//                        adapter.setItems(JsonReader.getProfiles(s));
//                    }
//                });
//            }
//        });
    }

    @Override
    public void showList(List<UserPreview> users, String nextUrl) {
        adapter.addItems(users);
        adapter.setNextUrl(nextUrl);
        mRecyclerView.finishLoading();
    }

    @Override
    public void failToRemove() {
        Toast.makeText(getActivity(), R.string.fail_to_remove_concern, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removed(int id) {
        Toast.makeText(getActivity(), R.string.removed_concern, Toast.LENGTH_SHORT).show();
    }
}
