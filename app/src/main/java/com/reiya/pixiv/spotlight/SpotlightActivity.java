package com.reiya.pixiv.spotlight;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.reiya.pixiv.adapter.ArticleAdapter;
import com.reiya.pixiv.base.BaseActivity;
import com.reiya.pixiv.bean.Article;
import com.reiya.pixiv.bean.Theme;
import com.reiya.pixiv.view.DividerItemDecoration;
import com.reiya.pixiv.view.LoaderRecyclerView;

import java.util.ArrayList;
import java.util.List;

import tech.yojigen.pivisionm.R;

public class SpotlightActivity extends BaseActivity<SpotlightPresenter> implements SpotlightContract.View {
    private ArticleAdapter adapter;
    private LinearLayoutManager layoutManager;
    private LoaderRecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Theme.getTheme());
        setContentView(R.layout.activity_spotlight);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24px);

        mRecyclerView = (LoaderRecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        adapter = new ArticleAdapter(this, new ArrayList<Article>());
        mRecyclerView.setAdapter(adapter);

//        long time = BaseApplication.getTime(SPOTLIGHT);
//        String cache = BaseApplication.getCache(SPOTLIGHT);
//        if (System.currentTimeMillis() - time > 1000 * 60 * 30) {

        mPresenter = new SpotlightPresenter();
        mPresenter.setView(this);
        mPresenter.loadList();
//            OldHttpClient.requestWithBearer(String.format(Value.URL_SPOTLIGHT, 1), new Callback() {
//                @Override
//                public void onFailure(Request request, IOException e) {
//                    Log.e("ERR", ":" + e.getMessage());
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(), R.string.fail_to_load, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//
//                @Override
//                public void onResponse(Response response) throws IOException {
//                    final String s = response.body().string();
//                    BaseApplication.writeCache(SPOTLIGHT, System.currentTimeMillis(), s);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed()) {
//                        return;
//                    }
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            adapter = new ArticleAdapter(getApplicationContext(), JsonReader.getArticles(s));
//                            adapter.setOnItemClickListener(new ArticleAdapter.OnItemClickListener() {
//                                @Override
//                                public void onClick(Article article) {
//                                    Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
//                                    intent.putExtra("works", article.getWorks());
//                                    intent.putExtra("date", article.getDate());
//                                    intent.putExtra("title", article.getTitle());
//                                    intent.putExtra("caption", article.getCaption());
//                                    startActivity(intent);
//                                }
//                            });
//                            recyclerView.setAdapter(adapter);
//                        }
//                    });
//                }
//            });
//        } else {
//            adapter = new ArticleAdapter(getApplicationContext(), JSON.parseObject(cache, new TypeReference<List<Article>>() {}));
//            setOnItemClickListener();
//        }

        mRecyclerView.setOnLoadMoreListener(new LoaderRecyclerView.OnLoadMoreListener() {
            @Override
            public void load() {
                if (adapter.getNextUrl() != null) {
                    mPresenter.loadMore(adapter.getNextUrl());
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void showList(List<Article> articles, String nextUrl) {
        adapter.addItems(articles);
        adapter.setNextUrl(nextUrl);
    }

    @Override
    public void showMore(List<Article> articles, String nextUrl) {
        adapter.addItems(articles);
        adapter.setNextUrl(nextUrl);
        mRecyclerView.finishLoading();
    }
}
