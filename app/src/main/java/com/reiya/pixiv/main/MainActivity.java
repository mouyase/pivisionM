package com.reiya.pixiv.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.reiya.pixiv.base.BaseApplication;
import com.reiya.pixiv.bean.Theme;
import com.reiya.pixiv.bean.User;
import com.reiya.pixiv.bean.Work;
import com.reiya.pixiv.dialog.AboutDialog;
import com.reiya.pixiv.dialog.LoginDialog;
import com.reiya.pixiv.download.DownloadActivity;
import com.reiya.pixiv.history.HistoryActivity;
import com.reiya.pixiv.image.ImageLoader;
import com.reiya.pixiv.other.LoginActivity;
import com.reiya.pixiv.other.SettingsActivity;
import com.reiya.pixiv.ranking.RankingActivity;
import com.reiya.pixiv.search.SearchActivity;
import com.reiya.pixiv.spotlight.SpotlightActivity;
import com.reiya.pixiv.util.TimeUtil;
import com.reiya.pixiv.util.UserData;
import com.tencent.bugly.beta.Beta;

import org.apache.commons.lang3.StringUtils;

import tech.yojigen.common.util.SettingUtil;
import tech.yojigen.pivisionm.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int NUM_CLICK_TOTAL_TO_DO_SOMETHING = 7;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private ImageView ivProfile;
    private TextView tvName;
    private DrawerLayout drawerLayout;
    private int numClickCount = 0;
    private BroadcastReceiver mReceiver;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        Handler handler = new Handler();
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//                String account = sharedPreferences.getString(getString(R.string.key_account), "");
//                String password = sharedPreferences.getString(getString(R.string.key_password), "");
//                Pixiv.getPixiv().accountLogin(account, password, new PixivListener() {
//                    @Override
//                    public void onFailure(IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onResponse(String json) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(json);
//                            String token = jsonObject.getJSONObject("response").getString("access_token");
//                            UserData.setBearer(token);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                handler.postDelayed(this, 1000 * 60 * 5);
//            }
//        };
//        handler.postDelayed(runnable, 1000 * 60 * 5);


        setTheme(Theme.getTheme());
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24px);

//        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbarLayout);
//        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
//        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.setToolbarNavigationClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        View header = navigationView.getHeaderView(0);
        ivProfile = header.findViewById(R.id.ivProfile);
        ivProfile.setOnClickListener(this);
        tvName = header.findViewById(R.id.tvName);
        navigationView.setNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.collection:
                    UserData.openCollection(MainActivity.this);
                    break;
                case R.id.concern:
                    UserData.openConcern(MainActivity.this);
                    break;
//                    case R.id.history_ranking:
//                        intent = new Intent(getApplicationContext(), HistoryRankingActivity.class);
//                        startActivity(intent);
//                        break;
                case R.id.history:
                    intent = new Intent(getApplicationContext(), HistoryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.download:
                    intent = new Intent(getApplicationContext(), DownloadActivity.class);
                    startActivity(intent);
                    break;
                case R.id.spotlight:
                    intent = new Intent(getApplicationContext(), SpotlightActivity.class);
                    startActivity(intent);
                    break;
                case R.id.settings:
                    intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivityForResult(intent, 1);
                    break;
                case R.id.about:
                    AboutDialog aboutDialog = new AboutDialog();
                    aboutDialog.show(getSupportFragmentManager(), "About");
                    break;
                case R.id.update:
                    Beta.checkUpgrade(true, false);
                    break;
            }
            return false;
        });

        NetworkInfo info = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null) {
            Toast.makeText(this, R.string.no_network_now, Toast.LENGTH_SHORT).show();
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            Toast.makeText(this, R.string.mobile_network_now, Toast.LENGTH_SHORT).show();
        }

        mViewPager = findViewById(R.id.viewPager);
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);

        if (!UserData.isLoggedIn()) {
            ((BaseApplication) getApplication()).tryLogin(this, false, new BaseApplication.OnLoginDone() {
                @Override
                public void onLoginDone(User user) {
                    showUserInfoOnNavigationHeader(user);
                    mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
                    mTabLayout.setupWithViewPager(mViewPager);
                }
            });
        } else {
            User user = BaseApplication.getUser();
            showUserInfoOnNavigationHeader(user);
        }

        String version = BaseApplication.getAppVersionName(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean(version, true)
                && preferences.getLong(getString(R.string.key_show_about_dialog_time), 0) + TimeUtil.DAY * 3 < System.currentTimeMillis()) {
            AboutDialog aboutDialog = new AboutDialog();
            aboutDialog.show(getSupportFragmentManager(), "About");
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            editor.putBoolean(version, false);
            editor.putLong(getString(R.string.key_show_about_dialog_time), System.currentTimeMillis());
            editor.apply();
        }

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (mToast == null) {
                    mToast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
                }
                mToast.setText(intent.getStringExtra("info"));
                mToast.show();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.UPDATE_PROGRESS");
        registerReceiver(mReceiver, filter);
    }

    public void setHeader(Work work) {
        ImageView ivHeader = (ImageView) findViewById(R.id.ivHeader);
        ImageLoader.loadImage(MainActivity.this, work.getImageUrl(1))
                .centerCrop()
                .load(ivHeader);
        ivHeader.setOnClickListener(MainActivity.this);
        ((TextView) findViewById(R.id.tvTitle)).setText(work.getTitle());
    }

    private void showUserInfoOnNavigationHeader(User user) {
        if (user == null) {
            return;
        }
        ImageLoader.loadImage(this, user.getProfileImageUrl())
                .centerCrop()
                .load(ivProfile);
        tvName.setText(user.getName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivProfile:
                if (!UserData.isLoggedIn()) {
                    BaseApplication.OnLoginDone onLoginDone = user -> {
                        showUserInfoOnNavigationHeader(user);
                        mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
                        mTabLayout.setupWithViewPager(mViewPager);
                    };
                    BaseApplication.OnLoginFailed onLoginFailed = () -> {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    };
//                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//                    String account = sharedPreferences.getString(getString(R.string.key_account), "");
//                    String password = sharedPreferences.getString(getString(R.string.key_password), "");
                    String refresh_token = SettingUtil.getSetting(this, "account_refresh_token", "");
                    if (!StringUtils.isEmpty(refresh_token)) {
                        LoginDialog loginDialog = new LoginDialog();
                        loginDialog.setListener(() -> BaseApplication.getInstance().login("", onLoginDone, onLoginFailed));
                        loginDialog.show(getSupportFragmentManager(), "Login");
                    } else {
                        BaseApplication.getInstance().login("", onLoginDone, onLoginFailed);
                    }
                }
                if (UserData.isLoggedIn()) {
                    numClickCount++;
                }
                if (numClickCount == NUM_CLICK_TOTAL_TO_DO_SOMETHING) {
                    UserData.setSpecialMode(true);
                    Toast.makeText(this, "新世界的大门", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ivHeader:
                startActivity(new Intent(this, RankingActivity.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
//            case R.id.download:
//                intent = new Intent(this, DownloadActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.subscription:
//                UserData.openNewWorks(this);
//                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                recreate();
                overridePendingTransition(0, 0);
                break;
            case 2:
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    if (fragment instanceof MainFragment) {
                        ((MainFragment) fragment).changeLayoutManager();
                    }
                }
                break;
            case 3:
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    if (fragment instanceof MainFragment) {
                        ((MainFragment) fragment).changeListStyle();
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        UserData.setSpecialMode(false);
        unregisterReceiver(mReceiver);
        System.exit(0);
        super.onDestroy();
    }

    private class PagerAdapter extends FragmentPagerAdapter {
        private final String[] TITLE = {getString(R.string.recommend), getString(R.string.subscription)};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return RecommendFragment.newInstance();
                case 1:
                    return SubscribeFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLE[position];
        }
    }
}
