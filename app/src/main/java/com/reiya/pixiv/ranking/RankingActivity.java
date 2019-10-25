package com.reiya.pixiv.ranking;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.reiya.pixiv.base.BaseFragment;
import com.reiya.pixiv.bean.Theme;
import com.reiya.pixiv.util.StringHelper;
import com.reiya.pixiv.util.UserData;

import java.util.Calendar;

import tech.yojigen.pivisionm.R;

public class RankingActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MyFragmentPagerAdapter mFragmentPagerAdapter;
    private Calendar mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Theme.getTheme());
        setContentView(R.layout.activity_ranking);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24px);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        mFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), UserData.getSpecialMode() ? 1 : 0);
        viewPager.setAdapter(mFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        mDate = Calendar.getInstance();
        mDate.set(Calendar.DAY_OF_YEAR, mDate.get(Calendar.DAY_OF_YEAR) - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, R.string.history_ranking).setIcon(R.drawable.ic_date_range_white_24px).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case 0:
                new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
                    mDate.set(year, monthOfYear, dayOfMonth);
                    String str = StringHelper.getFormattedDate(year, monthOfYear, dayOfMonth, "-");
                    System.out.println(str);
                    viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), UserData.getSpecialMode() ? 1 : 0, str));
                    viewPager.requestFocus();
                    tabLayout.setupWithViewPager(viewPager);
                }, mDate.get(Calendar.YEAR), mDate.get(Calendar.MONTH), mDate.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
//            int color = data.getIntExtra("color", 0);
//            appBarLayout.setBackgroundColor(color);
//            drawerHeader.setBackgroundColor(color);
            recreate();
            overridePendingTransition(0, 0);
        }
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private final int[] COUNT = {3, 7};
        //        private final String[][] titles = {{getString(R.string.daily), getString(R.string.weekly), getString(R.string.monthly)}, {getString(R.string.daily), getString(R.string.weekly), getString(R.string.male), getString(R.string.female)}};
        private final String[] titles = {getString(R.string.daily), getString(R.string.weekly), getString(R.string.monthly), getString(R.string.daily) + getString(R.string.r18), getString(R.string.weekly) + getString(R.string.r18), getString(R.string.male) + getString(R.string.r18), getString(R.string.female) + getString(R.string.r18)};
        private int mMode = 0;
        private String mDate;

        MyFragmentPagerAdapter(FragmentManager fm, int mode, String date) {
            super(fm);
            if (fm.getFragments() != null) {
                fm.getFragments().clear();
                getSupportFragmentManager();
            }
            mMode = mode;
            mDate = date;
            //修复日期选择不可用
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            for (Fragment fragment : fm.getFragments()) {
                fragmentTransaction.remove(fragment);
            }
            fragmentTransaction.commitNow();
        }

        MyFragmentPagerAdapter(FragmentManager fm, int mode) {
            this(fm, mode, null);
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = RankingFragment.newInstance(position, mMode, mDate);
            return fragment;
        }

        @Override
        public int getCount() {
            return COUNT[mMode];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

//        @Override
//        public int getItemPosition(Object object) {
//            return POSITION_NONE;
//        }

        void set() {
            mMode = 1;
            notifyDataSetChanged();
        }
    }
//
//    private void tryLogin() {
//        if (!User.token.equals(Value.DEFAULT_TOKEN)) {
//            return;
//        }
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String account = sharedPreferences.getString(getString(R.string.key_account), "");
//        String password = sharedPreferences.getString(getString(R.string.key_password), "");
//        if (account.equals("") || password.equals("")) {
//            LoginDialog loginDialog = new LoginDialog();
//            loginDialog.setListener(new LoginDialog.LoginListener() {
//                @Override
//                public void onLogin(String account, String password) {
//                    RankingActivity.this.onLogin(account, password, true);
//                }
//            });
//            loginDialog.show(getSupportFragmentManager(), "Login");
//        } else {
//            onLogin(account, password, false);
//        }
//    }
//
//    private void onLogin(final String account, final String password, final boolean save) {
//        if (BaseApplication.getAuthTime() + AUTH_EXPIRE_TIME > System.currentTimeMillis()) {
//            //            Log.e("BEARER", ":" + token);
//            User.token = BaseApplication.getToken();
//            User user = JsonReader.getUser(BaseApplication.getUser());
//            if (user != null) {
//                GlideUrl glideUrl = new GlideUrl(user.getUrl(), new LazyHeaders.Builder()
//                        .addHeader("Referer", "http://www.pixiv.net")
//                        .build());
//                Glide.with(this)
//                        .load(glideUrl)
//                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                        .into(ivProfile);
//                tvName.setText(user.getName());
//            }
//        } else {
//            Toast.makeText(this, R.string.logining, Toast.LENGTH_SHORT).show();
//            MultipartBuilder builder = new MultipartBuilder();
//            builder.type(MediaType.parse("multipart/form-data"))
//                    .addPart(Headers.of("Content-Disposition", "form-data; name=\"username\""), RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), account))
//                    .addPart(Headers.of("Content-Disposition", "form-data; name=\"password\""), RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), password))
//                    .addPart(Headers.of("Content-Disposition", "form-data; name=\"grant_type\""), RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), "password"))
//                    .addPart(Headers.of("Content-Disposition", "form-data; name=\"client_id\""), RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), "BVO2E8vAAikgUBW8FYpi6amXOjQj"))
//                    .addPart(Headers.of("Content-Disposition", "form-data; name=\"client_secret\""), RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), "LI1WsFUDrrquaINOdarrJclCrkTtc3eojCOswlog"))
//                    .addPart(Headers.of("Content-Disposition", "form-data; name=\"device_token\""), RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), ""));
//            HttpClient.requestWithBearer(Value.URL_AUTH, "POST", builder.build(), new Callback() {
//                @Override
//                public void onFailure(Request request, IOException e) {
////                    Log.e("ERR", ":" + e.getMessage());
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(), R.string.fail_to_login_network_err, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//
//                @Override
//                public void onResponse(Response response) throws IOException {
//                    String s = response.body().string();
////                    Log.e("LOGIN", s);
//                    String token = JsonReader.getToken(s);
//                    if (token.equals("")) {
////                        Log.e("LOGIN", "FAILED");
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(getApplicationContext(), R.string.fail_to_login_account_err, Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    } else {
////                        new LooperToast("登录成功").show();
//                        BaseApplication.writeToken(token);
//                        BaseApplication.writeUser(s);
////                        Log.e("BEARER", ":" + token);
//                        User.token = token;
//                        final User user = JsonReader.getUser(s);
//                        if (user != null) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(getApplicationContext(), R.string.login_successfully, Toast.LENGTH_SHORT).show();
//                                    GlideUrl glideUrl = new GlideUrl(user.getUrl(), new LazyHeaders.Builder()
//                                            .addHeader("Referer", "http://www.pixiv.net")
//                                            .build());
//                                    Glide.with(getApplicationContext())
//                                            .load(glideUrl)
//                                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                                            .into(ivProfile);
//                                    tvName.setText(user.getName());
//                                }
//                            });
//                        }
//                        if (save) {
//                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
//                            editor.putString(getString(R.string.key_account), account);
//                            editor.putString(getString(R.string.key_password), password);
//                            editor.putBoolean(getString(R.string.key_auto_login), true);
//                            editor.apply();
//                        }
//                    }
//                }
//            });
//        }
//    }
}
