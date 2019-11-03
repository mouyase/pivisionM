package com.reiya.pixiv.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.reiya.pixiv.adapter.TrendTagAdapter;
import com.reiya.pixiv.base.BaseApplication;
import com.reiya.pixiv.bean.Theme;
import com.reiya.pixiv.network.HttpService;
import com.reiya.pixiv.network.NetworkRequest;
import com.reiya.pixiv.view.WorkGridLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Subscriber;
import tech.yojigen.pivisionm.R;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int RESULT_KEYWORD = 111;
    private String mKeyword;
    private TextView mTextView;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Theme.getTheme());
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24px);

        mViewPager = findViewById(R.id.viewPager);
        mTabLayout = findViewById(R.id.tabLayout);
        mTextView = findViewById(R.id.textView);
        ImageButton btnSearch = findViewById(R.id.btnSearch);
//        ListView listView = (ListView) findViewById(R.id.searchListView);
        mRecyclerView = findViewById(R.id.recyclerView);

        mKeyword = getIntent().getStringExtra("tag");
        mTextView.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

        if (mKeyword != null) {
            mTextView.setText(mKeyword);
            search();
        } else {
            mTextView.setText("");
        }

        mRecyclerView.setLayoutManager(new WorkGridLayoutManager(this));

        NetworkRequest.getTrendTags().subscribe(new Subscriber<HttpService.TrendTagsResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(HttpService.TrendTagsResponse trendTagsResponse) {
                TrendTagAdapter adapter = new TrendTagAdapter(getApplicationContext(), trendTagsResponse.getTrendTags());
                adapter.setOnSearchTag(tag -> {
                    mKeyword = tag;
                    mTextView.setText(mKeyword);
                    search();
                });
                mRecyclerView.setAdapter(adapter);
            }
        });
    }

    private void search() {
        mRecyclerView.setVisibility(View.GONE);
        View view = SearchActivity.this.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        final List<String> history = new ArrayList<>(Arrays.asList(BaseApplication.getHistory()));
        if (!history.contains(mKeyword)) {
            if (history.size() > 4) {
                history.remove(history.size() - 1);
            }
        } else {
            history.remove(mKeyword);
        }
        history.add(0, mKeyword);
        BaseApplication.writeHistory(history.toArray(new String[history.size()]));
        mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        mViewPager.requestFocus();
        mTabLayout.setVisibility(View.VISIBLE);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_KEYWORD) {
            mKeyword = data.getStringExtra("text");
            mTextView.setText(mKeyword);
            trySearch();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
            case R.id.textView:
                Intent intent = new Intent(this, KeywordActivity.class);
                intent.putExtra("text", mTextView.getText().toString());
                startActivityForResult(intent, 1);
                overridePendingTransition(0, 0);
                break;
        }
    }

    private void trySearch() {
        String s = mTextView.getText().toString();
        if (s.equals("")) {
            Toast.makeText(getApplicationContext(), R.string.please_input_keyword, Toast.LENGTH_SHORT).show();
        } else {
            mKeyword = s;
            search();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        final int COUNT = 4;
        private final String[] titles = new String[]{"★10000", "★5000", "★1000", "全部"};

        MyFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            //修复搜索不可用
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            for (Fragment fragment : fragmentManager.getFragments()) {
                fragmentTransaction.remove(fragment);
            }
            fragmentTransaction.commitNow();
        }


        @Override
        public Fragment getItem(int position) {
            return SearchFragment.newInstance(position, mKeyword);
        }

        @Override
        public int getCount() {
            return COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
