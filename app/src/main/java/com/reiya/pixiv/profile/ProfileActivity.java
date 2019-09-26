package com.reiya.pixiv.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import tech.yojigen.pivisionm.R;
import com.reiya.pixiv.base.BaseFragment;
import com.reiya.pixiv.bean.Profile;
import com.reiya.pixiv.bean.Theme;
import com.reiya.pixiv.bean.User;
import com.reiya.pixiv.dialog.DownloadAllDialog;
import com.reiya.pixiv.image.ImageLoader;
import com.reiya.pixiv.util.UserOperation;

public class ProfileActivity extends AppCompatActivity {
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView ivProfile;
    private User mUser;
    private Profile mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Theme.getTheme());
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24px);
        getSupportActionBar().setTitle("");

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbarLayout);
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        mCollapsingToolbarLayout.setTitle("");
        ivProfile = (ImageView) findViewById(R.id.ivProfile);

        int id = getIntent().getIntExtra("id", 0);
        String data = getIntent().getDataString();
        if (data != null) {
            id = getId(data);
        }

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

//        if (!UserData.isLoggedIn()) {
//            final int finalId = id;
//            ((BaseApplication) getApplication()).tryLogin(this, false, new BaseApplication.OnLoginDone() {
//                @Override
//                public void onLoginDone(User user) {
//                    viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), getApplicationContext(), finalId));
//                    tabLayout.setupWithViewPager(viewPager);
//                }
//            });
//        } else {
//            viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), this, id));
//            tabLayout.setupWithViewPager(viewPager);
//        }

        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), this, id));
        tabLayout.setupWithViewPager(viewPager);

//        final WorkGridLayoutManager layoutManager = new WorkGridLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        adapter = new ImageAdapter(this, new ArrayList<SimpleItem>());
//        adapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int id, List<SimpleItem> list, int position) {
//                Intent intent = new Intent(getApplicationContext(), ViewActivity.class);
//                intent.putExtra("id", id);
//                intent.putExtra("items", (ArrayList) list);
//                intent.putExtra("position", position);
//                startActivity(intent);
//            }
//        });
//        recyclerView.setAdapter(adapter);
//
//        HttpClient.requestWithBearer(String.format(Value.URL_PROFILE, id), new Callback() {
//            @Override
//            public void onFailure(Request requestReturnCall, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                profile = JsonReader.getProfile(response.body().string());
//                if (profile == null) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(), R.string.invalid_object, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    return;
//                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed()) {
//                    return;
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        GlideUrl glideUrl = new GlideUrl(profile.getUrl(), new LazyHeaders.Builder()
//                                .addHeader("Referer", "http://www.pixiv.net")
//                                .build());
//                        Glide.with(getApplicationContext())
//                                .load(glideUrl)
//                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                                .into(ivProfile);
//                        getSupportActionBar().setTitle(profile.getName());
//                        tvComment.setText(profile.getComment());
//                        invalidateOptionsMenu();
//                    }
//                });
//            }
//        });
//        HttpClient.requestWithBearer(String.format(Value.URL_WORKS, id, 1), new Callback() {
//            @Override
//            public void onFailure(Request requestReturnCall, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                final List<SimpleItem> items = JsonReader.getWorks(response.body().string());
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        adapter.setItems(items);
//                    }
//                });
//            }
//        });
//
//        final int finalId = id;
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            int lastVisibleItem;
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE
//                        && lastVisibleItem + 1 == adapter.getItemCount() && !adapter.isLoading()) {
//                    adapter.setLoading(true);
//                    HttpClient.requestWithBearer(String.format(Value.URL_WORKS, finalId, adapter.getPage() + 1), new Callback() {
//                        @Override
//                        public void onFailure(Request requestReturnCall, IOException e) {
//
//                        }
//
//                        @Override
//                        public void onResponse(Response response) throws IOException {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed()) {
//                                return;
//                            }
//                            ResponseBody responseBody = response.body();
//                            final String s = responseBody.string();
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    adapter.addItems(JsonReader.getWorks(s));
//                                }
//                            });
//                            adapter.setLoading(false);
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
//            }
//        });

    }

    private int getId(String string) {
        String[] a = string.replaceAll("[^0-9]", "/").split("/");
        for (String s : a) {
            if (!s.equals("")) {
                return Integer.parseInt(s);
            }
        }
        return 0;
    }

    public void showUserInfo() {
        String url = mUser.getMediumImageUrl();
        ImageLoader.loadImage(this, url)
                .centerCrop()
                .load(ivProfile);
        mCollapsingToolbarLayout.setTitle(mUser.getName());
        final CheckBox cbFollow = (CheckBox) findViewById(R.id.btnFollow);
        cbFollow.setChecked(mUser.isFollowed());
        cbFollow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mUser.isFollowed() != isChecked) {
                    mUser.setFollowed(isChecked);
                    if (isChecked) {
                        UserOperation.favorite(ProfileActivity.this, mUser.getId(), "public");
//                    ItemOperation.addBookmark(this, mWork);
                        if (PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this)
                                .getBoolean(getString(R.string.key_show_toast_long_click_follow), true)) {
                            Toast.makeText(ProfileActivity.this, R.string.toast_long_click_private, Toast.LENGTH_LONG).show();
                            PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this).edit()
                                    .putBoolean(getString(R.string.key_show_toast_long_click_follow), false)
                                    .apply();
                        }
                    } else {
                        UserOperation.outFavorite(ProfileActivity.this, mUser.getId());
                    }
                }
            }
        });
        cbFollow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!cbFollow.isChecked()) {
                    UserOperation.favorite(ProfileActivity.this, mUser.getId(), "private");
                    mUser.setFollowed(true);
                    cbFollow.setChecked(true);
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.add(0, 0, 0, R.string.concern).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(1, 1, 1, R.string.save_all_works).setIcon(R.drawable.ic_file_download_white_24px).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(2, 2, 2, "分享").setIcon(R.drawable.ic_share_white_24px).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        int res;
//        if (mUser != null && mUser.isFollowed()) {
//            res = R.drawable.ic_person_done_white_24px;
//        } else {
//            res = R.drawable.ic_person_add_white_24px;
//        }
//        menu.getItem(0).setIcon(res);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case 0:
//                if (mUser != null) {
//                    boolean isFollowed = mUser.isFollowed();
//                    if (isFollowed) {
//                        UserOperation.removeBookmark(this, mUser.getId());
//                    } else {
//                        UserOperation.favourite(this, mUser.getId());
//                    }
//                    mUser.setFollowed(!isFollowed);
//                    invalidateOptionsMenu();
//                }
//                break;
            case 1:
                if (mUser == null || mProfile == null) {
                    return true;
                }
                DownloadAllDialog downloadAllDialog = new DownloadAllDialog();
                downloadAllDialog.setType(DownloadAllDialog.TYPE_AUTHOR);
                downloadAllDialog.setUserId(mUser.getId());
                downloadAllDialog.show(getSupportFragmentManager(), "DownloadAll");
                break;
            case 2:
                if (mUser == null) {
                    return true;
                }
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, mUser.getName() + "\n" + "http://www.pixiv.net/member.php?id=" + mUser.getId() + "\n" + "#pixiv");
                startActivity(intent);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public void setProfile(Profile profile) {
        mProfile = profile;
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        public final int COUNT = 2;
        private final String[] titles = new String[]{getString(R.string.works), getString(R.string.personal_info)};
        private final Context context;
        private int id = 0;

        public MyFragmentPagerAdapter(FragmentManager fm, Context context, int id) {
            super(fm);
            this.context = context;
            this.id = id;
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = ProfileFragment.newInstance(position, id);
            return fragment;
        }

        @Override
        public int getCount() {
            return COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
