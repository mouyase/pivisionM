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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.reiya.pixiv.adapter.TrendTagAdapter;
import com.reiya.pixiv.base.BaseApplication;
import com.reiya.pixiv.base.BaseFragment;
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
    private String mKeyword;
    private TextView mTextView;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private RecyclerView mRecyclerView;
    public static final int RESULT_KEYWORD = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Theme.getTheme());
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24px);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTextView = (TextView) findViewById(R.id.textView);
        final ImageButton btnSearch = (ImageButton) findViewById(R.id.btnSearch);
//        ListView listView = (ListView) findViewById(R.id.searchListView);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mKeyword = getIntent().getStringExtra("tag");
        mTextView.setOnClickListener(this);
//        String[] s = BaseApplication.getHistory();
//        mSearchAdapter = new SearchAdapter(this, Arrays.asList(s));
//        mSearchAdapter.setOnTextSelected(new SearchAdapter.OnTextSelected() {
//            @Override
//            public void onTextSelected(String s) {
//                mKeyword = s;
//                mTextView.setText(mKeyword);
//                mTextView.setClearIconVisible(true);
//                btnSearch.performClick();
//            }
//        });
//        mTextView.setAdapter(mSearchAdapter);
////        listView.setAdapter(mSearchAdapter);
//        mTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                Log.e("focus", "" + hasFocus);
////                if (hasFocus) {
////                    mSearchAdapter.open();
////                } else {
////                    mSearchAdapter.close();
////                }
//            }
//        });
//        mTextView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                    trySearch();
//                    return true;
//                }
//                return false;
//            }
//        });
//        mTextView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
////                mSearchAdapter.getFilter().filter(s);
//                showPopupMenu(s.toString());
//            }
//        });

        if (mKeyword != null) {
            mTextView.setText(mKeyword);
            search();
        } else {
//            mSearchAdapter.getFilter().filter("");
//            mSearchAdapter.close();
//            mRecyclerView.requestFocus();
            mTextView.setText("");
        }

        mRecyclerView.setLayoutManager(new WorkGridLayoutManager(this));

        NetworkRequest.getTrendTags()
                .subscribe(new Subscriber<HttpService.TrendTagsResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HttpService.TrendTagsResponse trendTagsResponse) {
                        TrendTagAdapter adapter = new TrendTagAdapter(getApplicationContext(), trendTagsResponse.getTrendTags());
                        adapter.setOnSearchTag(new TrendTagAdapter.OnSearchTag() {
                            @Override
                            public void onSearchTag(String tag) {
                                mKeyword = tag;
                                mTextView.setText(mKeyword);
                                search();
                            }
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
                trySearch();
                break;
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
//
//    private void showPopupMenu(String s) {
//        //参数View 是设置当前菜单显示的相对于View组件位置，具体位置系统会处理
//        PopupMenu popupMenu = new PopupMenu(this, mTextView);
//        //加载menu布局
//        List<SearchTextFilter.Item> items = SearchTextFilter.getItems(BaseApplication.getHistory(), s);
//        for (int i = 0, l = items.size(); i < l; i++) {
//            SearchTextFilter.Item item = items.get(i);
//            popupMenu.getMenu().add(item.type, item.id, i, item.text);
//        }
//
//        //设置menu中的item点击事件
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Intent intent;
//                switch (item.getGroupId()) {
//                    case -1:
//                        BaseApplication.writeHistory(new String[]{""});
//                        break;
//                    case 0:
//                        mKeyword = (String) item.getTitle();
//                        mTextView.setText(mKeyword);
//                        mTextView.setClearIconVisible(true);
//                        trySearch();
//                        break;
//                    case 1:
//                        intent = new Intent(getApplicationContext(), ViewActivity.class);
//                        intent.putExtra("id", item.getItemId());
//                        startActivity(intent);
//                        finish();
//                        break;
//                    case 2:
//                        intent = new Intent(getApplicationContext(), ProfileActivity.class);
//                        intent.putExtra("id", item.getItemId());
//                        startActivity(intent);
//                        finish();
//                        break;
//                }
//                return false;
//            }
//        });
//        //设置popupWindow消失的点击事件
//        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
//            @Override
//            public void onDismiss(PopupMenu menu) {
//
//            }
//        });
//
//        popupMenu.show();
//        mTextView.requestFocus();
//    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        public final int COUNT = 4;
        private final String[] titles = new String[]{"★10000", "★5000", "★1000", "全部"};

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            if (fm.getFragments() != null) {
                fm.getFragments().clear();
            }
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = SearchFragment.newInstance(position, mKeyword);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
