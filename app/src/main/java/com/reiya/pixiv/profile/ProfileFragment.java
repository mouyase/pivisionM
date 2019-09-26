package com.reiya.pixiv.profile;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import tech.yojigen.pivisionm.R;
import com.reiya.pixiv.adapter.ImageAdapter;
import com.reiya.pixiv.base.BaseFragment;
import com.reiya.pixiv.bean.Profile;
import com.reiya.pixiv.bean.User;
import com.reiya.pixiv.bean.Work;
import com.reiya.pixiv.view.LoaderRecyclerView;
import com.reiya.pixiv.view.WorkGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/3/4.
 */
public class ProfileFragment extends BaseFragment<ProfilePresenter> implements ProfileContract.View {
    private int mPage;
    private static final String ID = "id";
    private int id;
    private ImageAdapter adapter;
    private View mView;
    private ProfilePresenter mPresenter;
    private LoaderRecyclerView mRecyclerView;

    public static ProfileFragment newInstance(int page, int id) {
        Bundle args = new Bundle();
        args.putInt(PAGE, page);
        args.putInt(ID, id);
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getInt(ID);
        mPage = getArguments().getInt(PAGE);
        mPresenter = new ProfilePresenter();
        mPresenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        switch (mPage) {
            case 1:
                mView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_profile_info, null);
                mView.setVisibility(View.INVISIBLE);

                return mView;
            case 0:
                mRecyclerView = (LoaderRecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
                mRecyclerView.setLayoutManager(new WorkGridLayoutManager(getActivity()));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                adapter = new ImageAdapter(getActivity(), new ArrayList<Work>());
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.setOnLoadMoreListener(new LoaderRecyclerView.OnLoadMoreListener() {
                    @Override
                    public void load() {
                        if (adapter.getNextUrl() != null) {
                            mPresenter.getMore(adapter.getNextUrl());
                        }
                    }
                });

                return mRecyclerView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        switch (mPage) {
            case 0:
                mPresenter.getWorks(id);
                break;
            case 1:
                mPresenter.getUser(id);
                break;
        }
    }

    @Override
    public void showWorks(List<Work> works, String nextUrl) {
        adapter.addItems(works);
        adapter.setNextUrl(nextUrl);
        adapter.setLoading(false);
    }

    @Override
    public void showMore(List<Work> works, String nextUrl) {
        adapter.addItems(works);
        adapter.setNextUrl(nextUrl);
        mRecyclerView.finishLoading();
    }

    @Override
    public void showUserInfo(User user, Profile profile) {
        if (user == null) {
            if (getActivity() == null) {
                return;
            }
            Toast.makeText(getActivity(), R.string.invalid_object, Toast.LENGTH_SHORT).show();
        } else {
            mView.setVisibility(View.VISIBLE);


            ((TextView) mView.findViewById(R.id.tvName)).setText(user.getName() + "\n(" + user.getId() + ")");
            ((TextView) mView.findViewById(R.id.tvInfo)).setText(profile.getInfo(getActivity()));
            ((TextView) mView.findViewById(R.id.tvHomepage)).setText(profile.getWebpage());
            ((TextView) mView.findViewById(R.id.tvComment)).setText(Html.fromHtml(user.getComment()));

            if (getActivity() == null) {
                return;
            }

            ((ProfileActivity) getActivity()).setUser(user);
            ((ProfileActivity) getActivity()).showUserInfo();
            ((ProfileActivity) getActivity()).setProfile(profile);
            getActivity().invalidateOptionsMenu();
        }
    }

    @Override
    public void failToLoad() {
        if (getActivity() == null) {
            return;
        }
        Toast.makeText(getActivity(), R.string.fail_to_load, Toast.LENGTH_SHORT).show();
    }
}
